package com.example.demo;

import org.springframework.stereotype.Component;
import com.example.demo.domain.Poll;
import com.example.demo.domain.Vote;
import com.example.demo.domain.VoteOption;
import com.example.demo.domain.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class PollManager {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Poll> polls = new HashMap<>();
    private final Map<Long, Vote> votes = new HashMap<>();
    private final Map<Long, VoteOption> voteOptions = new HashMap<>();

    private long nextVoteOptionId = 1L;

    public PollManager() {}

    public Map<Long, User> getUsers() {
        return users;
    }

    public Map<Long, Poll> getPolls() {
        return polls;
    }

    public Map<Long, Vote> getVotes() {
        return votes;
    }

    public Map<Long, VoteOption> getVoteOptions() {
        return voteOptions;
    }
    public synchronized long getNextVoteOptionId() {
        return nextVoteOptionId++;
    }
}
