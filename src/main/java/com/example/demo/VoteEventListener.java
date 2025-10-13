package com.example.demo;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.demo.domain.VoteEvent;
import com.example.demo.domain.Vote;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class VoteEventListener {

    @Autowired
    private PollManager pollManager;

    @RabbitListener(queues = "#{T(java.util.Arrays).asList('poll-1', 'poll-2')}")
    public void handleVoteEvent(String message) {
        System.out.println("Received vote event: " + message);
        try {
            // Parse the JSON message into a VoteEvent object
            ObjectMapper mapper = new ObjectMapper();
            VoteEvent event = mapper.readValue(message, VoteEvent.class);

            // Only add the vote if the poll and option still exist
            if (pollManager.getPolls().containsKey(event.pollId) &&
                pollManager.getVoteOptions().containsKey(event.optionId)) {

                Vote vote = new Vote();
                vote.setId((long) pollManager.getVotes().size() + 1);
                vote.setPoll(pollManager.getPolls().get(event.pollId));
                vote.setVotesOn(pollManager.getVoteOptions().get(event.optionId));
                if (event.voterId != null) {
                    vote.setVoter(pollManager.getUsers().get(event.voterId));
                }
                pollManager.getVotes().put(vote.getId(), vote);
            } else {
                System.out.println("Ignored vote event for deleted poll or option: " + message);
            }

        } catch (Exception e) {
            System.err.println("Failed to parse vote event: " + e.getMessage());
        }
    }
}
