package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collection;
import com.example.demo.domain.Poll;
import com.example.demo.domain.VoteOption;

@CrossOrigin
@RestController
public class PollController {

    @Autowired
    private PollManager pollManager;

    public static class PollResponse {
        public Long id;
        public String question;
        public java.util.List<String> options;

        public PollResponse(Long id, String question, java.util.List<String> options) {
            this.id = id;
            this.question = question;
            this.options = options;
        }
    }

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
            option.setId((long) (pollManager.getVoteOptions().size() + 1));
            option.setCaption(options.get(i));
            option.setPollId(newId);
            option.setPresentationOrder(i);
            pollManager.getVoteOptions().put(option.getId(), option);
        }

        // Build response with option captions
        return new PollResponse(poll.getId(), poll.getQuestion(), options);
    }


    
    @GetMapping("/polls")
    public Collection<Poll> listPolls() {
        return pollManager.getPolls().values();
    }

    @DeleteMapping("/polls/{pollId}")
    public void deletePoll(@PathVariable Long pollId) {
        pollManager.getPolls().remove(pollId);

        pollManager.getVotes().entrySet().removeIf(entry -> 
            pollId.equals(entry.getValue().getPollId()) || entry.getValue().getPollId() == null
        );

        pollManager.getVoteOptions().values().removeIf(option -> pollId.equals(option.getPollId()));
    }
}
