package com.example.demo;

import org.springframework.stereotype.Component;

import com.example.demo.domain.Poll;
import com.example.demo.domain.Vote;
import com.example.demo.domain.VoteOption;
import com.example.demo.domain.User;

import java.util.HashMap;

@Component
public class PollManager {
    private static HashMap<Long, User> users= new HashMap<>();
    private static HashMap<Long, Poll> polls = new HashMap<>();
    private static HashMap<Long, Vote> votes = new HashMap<>();
    private static HashMap<Long, VoteOption> voteOptions = new HashMap<>();

    public PollManager() {}

    public HashMap<Long, User> getUsers() {
        return users;
    }

    public void setUsers(HashMap<Long, User> users) {
        this.users = users;
    }

    public HashMap<Long, Poll> getPolls() {
        return polls;
    }
    public void setPolls(HashMap<Long, Poll> polls) {
        this.polls = polls;
    }

    public HashMap<Long, Vote> getVotes() {
        return votes;
    }
    public void setVotes(HashMap<Long, Vote> votes) {
        this.votes = votes;
    }

    public HashMap<Long, VoteOption> getVoteOptions() {
        return voteOptions;
    }
    public void setVoteOptions(HashMap<Long, VoteOption> voteOptions) {
        this.voteOptions = voteOptions;
    }
}
