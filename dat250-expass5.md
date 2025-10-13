From Last week, managed to log into H2 console and see the database, with help from another student in the class. Seems that the URL in application.properties and Pollstest.java did not match up. So after fixing that the page finally works. 

For this task, everything has mostly gone pretty well. I used Redis with docker and it was nice to get some easy example at first so I could understand how Redis worked. Then when I implemented it, I mostly did everything in VoteController, except for invalidating the cache after a vote, which I did in Createvote. I struggled a bit with understanding what i should keep and what I should not in VoteController, but it works now so I think it is okay. To be safe, I have commented out the old code in Votecontroller, so that I can remove it once I am sure that I don't need it anymore. 

Another part that was perhaps unnecessarily complicated for me was testing to see if Redis worked with the application. What I did was:

1: Went on the webpage, made a poll and voted on the poll.
2: Used (curl http://localhost:8080/polls/1/votes) to see if it was collected from Redis or if it was aggregated from the DB.
3: checked Redis with (KEYS *) and (LRANGE poll:votes:1 0 -1) to see if the poll was added there. 
Posted a vote with (curl -X POST -H "Content-Type: application/json" \
  -d '{"poll": {"id": 1}, "votesOn": {"id": 1}, "voter": {"id": 1}}' \
  http://localhost:8080/votes) to see if this invalidated the cache. Checked this by using (KEYS *) in Redis.
4: Used (curl http://localhost:8080/polls/1/votes) twice, first time for cache miss, and the second time for cache hit.

Doing all of this, and also looking at the backend logs, gave me proof that Redis works as intended. Perhaps there is a easier way to test if it works, but this was the best solution I came up with.