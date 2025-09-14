import { useEffect, useState } from "react";

function Votepoll({ poll }) {
    const [votes, setVotes] = useState([]);
    
    if (!poll || !Array.isArray(poll.options)) return null;

    useEffect(() => {
        if (poll) {
            fetch(`/polls/${poll.id}/votes`)
            .then(res => res.json())
            .then(data => setVotes(Array.isArray(data) ? data : Array(poll.options.length).fill(0)))
            .catch(() => setVotes(Array(poll.options.length).fill(0)));
        }
    }, [poll]);

    const handleVote = (index) => {
        fetch(`/votes`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                pollId: poll.id,
                voteOptionId: poll.optionIds ? poll.optionIds[index] : index
            })
        })
        .then(res => res.json())
        .then(data => {
            setVotes(Array.isArray(data.votes) ? data.votes : votes.map((v, i) => i === index ? v + 1 : v));
        })
        .catch(() =>  {
            setVotes(Array.isArray(votes) ? votes.map((v, i) => i === index ? v + 1 : v) : []);
        });
    };

    if (!poll) return null; 

    return (
        <div style={{ padding: "20px", fontFamily: "sans-serif" }}>
            <h2>{poll.question}</h2>
            <ul>
                {poll.options.map((option, index) => (
                    <li key={option} style={{ margin: "8px 0" }}>
                        <button onClick={() => handleVote(index)}>{option}</button>
                        <span style={{ marginLeft: "10px" }}>
                            Votes: {votes[index] ?? 0}
                        </span>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default Votepoll;
