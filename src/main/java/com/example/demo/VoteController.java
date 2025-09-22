package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.domain.Vote;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class VoteController {

    @Autowired
    private PollManager pollManager;

    // ---------------------------
    // Create a vote
    // ---------------------------
    @PostMapping("/votes")
    public Vote createVote(@RequestBody Vote vote) {
        if (vote.getPoll() == null || vote.getVotesOn() == null || vote.getVoter() == null) {
            throw new IllegalArgumentException("poll, votesOn, and voter must not be null");
        }

        // Remove previous vote by this user for this poll
        pollManager.getVotes().entrySet().removeIf(entry ->
            entry.getValue().getVoter() != null &&
            entry.getValue().getPoll() != null &&
            vote.getVoter().getId().equals(entry.getValue().getVoter().getId()) &&
            vote.getPoll().getId().equals(entry.getValue().getPoll().getId())
        );

        long newId = pollManager.getVotes().size() + 1;
        vote.setId(newId);
        pollManager.getVotes().put(newId, vote);
        return vote;
    }

    // ---------------------------
    // List all raw votes (existing)
    // ---------------------------
    @GetMapping("/votes")
    public Collection<Vote> listVotes() {
        // Only return the most recent vote per user and poll
        Map<String, Vote> latestVotes = new HashMap<>();
        for (Vote vote : pollManager.getVotes().values()) {
            if (vote.getVoter() == null || vote.getPoll() == null) continue;
            String key = vote.getVoter().getId() + "-" + vote.getPoll().getId();
            Vote existing = latestVotes.get(key);
            if (existing == null || 
                (vote.getPublishedAt() != null && existing.getPublishedAt() != null &&
                 vote.getPublishedAt().isAfter(existing.getPublishedAt()))) {
                latestVotes.put(key, vote);
            }
        }
        return latestVotes.values();
    }

    // ---------------------------
    // New endpoint: return vote counts per option for a poll
    // ---------------------------
    @GetMapping("/polls/{pollId}/votes")
    public List<Integer> getVotesForPoll(@PathVariable Long pollId) {
        // Get all vote options for this poll, sorted by presentationOrder
        List<Long> optionIds = pollManager.getVoteOptions().values().stream()
                .filter(opt -> opt.getPoll() != null && pollId.equals(opt.getPoll().getId()))
                .sorted(Comparator.comparingInt(opt -> opt.getPresentationOrder()))
                .map(opt -> opt.getId())
                .collect(Collectors.toList());

        // Count votes for each option
        List<Integer> voteCounts = optionIds.stream()
                .map(id -> (int) pollManager.getVotes().values().stream()
                        .filter(v -> v.getVotesOn() != null && id.equals(v.getVotesOn().getId()))
                        .count())
                .collect(Collectors.toList());

        return voteCounts;
    }
}