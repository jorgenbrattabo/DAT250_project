package com.example.demo;

import com.example.demo.domain.User;
import com.example.demo.domain.Poll;
import com.example.demo.domain.Vote;
import com.example.demo.domain.VoteOption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FullScenarioTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void fullScenarioTest() {
        // 1. Create user 1
        User user1 = new User();
        user1.setUsername("Ola Normann");
        user1.setEmail("ola.normann@email.com");
        ResponseEntity<User> user1Response = restTemplate.postForEntity("/users", user1, User.class);
        Long user1Id = user1Response.getBody().getId();

        // 2. List users (should show user1)
        ResponseEntity<User[]> usersResponse1 = restTemplate.getForEntity("/users", User[].class);
        assertThat(usersResponse1.getStatusCode().value()).isEqualTo(200);
        assertThat(usersResponse1.getBody()).hasSize(1);


        // 3. Create user 2
        User user2 = new User();
        user2.setUsername("Kari Normann");
        user2.setEmail("kari.normann@email.com");
        ResponseEntity<User> user2Response = restTemplate.postForEntity("/users", user2, User.class);
        Long user2Id = user2Response.getBody().getId();

        // 4. List users again (should show two users)
        ResponseEntity<User[]> usersResponse2 = restTemplate.getForEntity("/users", User[].class);
        assertThat(usersResponse2.getBody()).hasSize(2);

        // 5. User 1 creates a poll
        Poll poll = new Poll();
        poll.setQuestion("What is your Favorite color?");
        poll.setPublishedAt(java.time.Instant.parse("2025-09-03T12:00:00Z"));
        poll.setValidUntil(java.time.Instant.parse("2025-09-10T12:00:00Z"));
        poll.setCreatedBy(user1);
        ResponseEntity<Poll> pollResponse = restTemplate.postForEntity("/polls", poll, Poll.class);
        Poll createdPoll = pollResponse.getBody(); // Use this for all further references


        // 6. Create vote options for the poll
        VoteOption option1 = new VoteOption();
        option1.setCaption("Red");
        option1.setPresentationOrder(1);
        option1.setPoll(createdPoll); // Use the poll returned from backend
        ResponseEntity<VoteOption> option1Response = restTemplate.postForEntity("/voteoptions", option1, VoteOption.class);
        VoteOption createdOption1 = option1Response.getBody();

        VoteOption option2 = new VoteOption();
        option2.setCaption("Blue");
        option2.setPresentationOrder(2);
        option2.setPoll(createdPoll);
        ResponseEntity<VoteOption> option2Response = restTemplate.postForEntity("/voteoptions", option2, VoteOption.class);
        VoteOption createdOption2 = option2Response.getBody();

        // 7. List polls (should show the new poll)
        ResponseEntity<Poll[]> pollsResponse = restTemplate.getForEntity("/polls", Poll[].class);
        assertThat(pollsResponse.getStatusCode().value()).isEqualTo(200);
        assertThat(pollsResponse.getBody()).hasSize(1);

        // 8. User 2 votes on the poll
        Vote vote1 = new Vote();
        vote1.setVoter(user2Response.getBody()); // Use the returned user
        vote1.setPoll(createdPoll);
        vote1.setVotesOn(createdOption1);
        vote1.setPublishedAt(java.time.Instant.parse("2025-09-03T12:05:00Z"));
        restTemplate.postForEntity("/votes", vote1, Vote.class);

        // 9. User 2 changes his vote
        Vote vote2 = new Vote();
        vote2.setVoter(user2Response.getBody());
        vote2.setPoll(createdPoll);
        vote2.setVotesOn(createdOption2);
        vote2.setPublishedAt(java.time.Instant.parse("2025-09-03T12:10:00Z"));
        restTemplate.postForEntity("/votes", vote2, Vote.class);

        // 10. List votes (should show the most recent vote for User 2)
        ResponseEntity<Vote[]> votesResponse = restTemplate.getForEntity("/votes", Vote[].class);
        assertThat(votesResponse.getStatusCode().value()).isEqualTo(200);
        System.out.println("Votes before assertion: " + votesResponse.getBody());
        assertThat(votesResponse.getBody()).isNotEmpty();

        // 11. Delete the poll
        restTemplate.delete("/polls/" + createdPoll.getId());

        // 12. List votes (should be empty)
        ResponseEntity<Vote[]> votesAfterDeleteResponse = restTemplate.getForEntity("/votes", Vote[].class);
        assertThat(votesAfterDeleteResponse.getBody()).isEmpty();
    }
}