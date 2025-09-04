package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collection;
import com.example.demo.domain.Poll;

@RestController
public class PollController {

    @Autowired
    private PollManager pollManager;

    @PostMapping("/polls")
    public Poll createPoll(@RequestBody Poll poll) {
        long newId = pollManager.getPolls().size() + 1;
        poll.setId(newId);
        pollManager.getPolls().put(newId, poll);
        return poll;
    }
    
    @GetMapping("/polls")
    public Collection<Poll> listPolls() {
        return pollManager.getPolls().values();
    }

    @DeleteMapping("/polls/{pollId}")
    public void deletePoll(@PathVariable Long pollId) {
        pollManager.getPolls().remove(pollId);
        pollManager.getVotes().values().removeIf(vote -> pollId.equals(vote.getPollId()));
        pollManager.getVoteOptions().values().removeIf(option -> pollId.equals(option.getPollId()));
    }
}
