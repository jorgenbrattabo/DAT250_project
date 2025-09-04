package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.domain.Vote;
import java.util.Collection;

@RestController
public class VoteController {
    
    
    @Autowired
    private PollManager pollManager;

    @PostMapping("/votes")
    public Vote createVote(@RequestBody Vote vote) {
        long newId = pollManager.getVotes().size() + 1;
        vote.setId(newId);
        pollManager.getVotes().put(newId, vote);
        return vote;
    }

    @GetMapping("/votes")
    public Collection<Vote> listVotes() {
        return pollManager.getVotes().values();
    }
}

