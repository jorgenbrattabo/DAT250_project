package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;
import com.example.demo.domain.Poll;
import com.example.demo.domain.VoteOption;

@CrossOrigin
@RestController
public class PollController {

    @Autowired
    private PollManager pollManager;

    public static class OptionResponse {
        public Long id;
        public String caption;
        public OptionResponse(Long id, String caption) {
            this.id = id;
            this.caption = caption;
        }
    }

    public static class PollResponse {
        public Long id;
        public String question;
        public java.util.List<OptionResponse> options;

        public PollResponse(Long id, String question, java.util.List<OptionResponse> options) {
            this.id = id;
            this.question = question;
            this.options = options;
        }
    }

    //Posting polls
    @PostMapping("/polls")
    public PollResponse createPoll(@RequestBody java.util.Map<String, Object> payload) {
        String question = (String) payload.get("question");
        java.util.List<String> options = (java.util.List<String>) payload.get("options");

        // Create poll
        long newId = pollManager.getPolls().size() + 1;
        Poll poll = new Poll();
        poll.setId(newId);
        poll.setQuestion(question);
        pollManager.getPolls().put(newId, poll);

        // Store each option
        for (int i = 0; i < options.size(); i++) {
            VoteOption option = new VoteOption();
            option.setId(pollManager.getNextVoteOptionId());
            option.setCaption(options.get(i));
            option.setPoll(poll);
            option.setPresentationOrder(i);
            pollManager.getVoteOptions().put(option.getId(), option);
        }

        // Build the list of option responses with id and caption
        java.util.List<OptionResponse> optionResponses = pollManager.getVoteOptions().values().stream()
            .filter(opt -> opt.getPoll().getId().equals(poll.getId()))
            .sorted(java.util.Comparator.comparingInt(VoteOption::getPresentationOrder))
            .map(opt -> new OptionResponse(opt.getId(), opt.getCaption()))
            .toList();

        return new PollResponse(poll.getId(), poll.getQuestion(), optionResponses);
    }

    // Getting the polls
    @GetMapping("/polls")
    public Collection<Poll> listPolls() {
        return pollManager.getPolls().values();
    }

    // Deleting polls
    @DeleteMapping("/polls/{pollId}")
    public void deletePoll(@PathVariable Long pollId) {
        pollManager.getPolls().remove(pollId);

        // Remove all votes for this poll
        pollManager.getVotes().entrySet().removeIf(entry ->
            entry.getValue().getPoll() != null &&
            pollId.equals(entry.getValue().getPoll().getId())
        );

        // Remove all vote options for this poll
        pollManager.getVoteOptions().entrySet().removeIf(entry ->
            entry.getValue().getPoll() != null &&
            pollId.equals(entry.getValue().getPoll().getId())
        );
    }
}
