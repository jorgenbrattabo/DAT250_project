package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.domain.Vote;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
public class VoteController {
    
    
    @Autowired
    private PollManager pollManager;

    @PostMapping("/votes")
    public Vote createVote(@RequestBody Vote vote) {
        if (vote.getPollId() == null || vote.getVoteOptionId() == null) {
            throw new IllegalArgumentException("pollId and voteOptionId must not be null");
        }

        // Removes previous vote by this user for this poll
        pollManager.getVotes().entrySet().removeIf(entry ->
            vote.getUserId().equals(entry.getValue().getUserId()) &&
            vote.getPollId().equals(entry.getValue().getPollId())
        );

        long newId = pollManager.getVotes().size() + 1;
        vote.setId(newId);
        pollManager.getVotes().put(newId, vote);
        return vote;
    }

    @GetMapping("/votes")
    public Collection<Vote> listVotes() {
        // Only return the most recent vote per user and poll
        Map<String, Vote> latestVotes = new HashMap<>();
        for (Vote vote : pollManager.getVotes().values()) {
            String key = vote.getUserId() + "-" + vote.getPollId();
            Vote existing = latestVotes.get(key);
            if (existing == null || 
                (vote.getPublishedAt() != null && existing.getPublishedAt() != null && vote.getPublishedAt().isAfter(existing.getPublishedAt()))) {
                latestVotes.put(key, vote);
            }
        }
        return latestVotes.values();
    }
}

