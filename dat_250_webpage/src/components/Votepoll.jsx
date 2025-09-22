import { useEffect, useState } from "react";

function Votepoll({ poll }) {
    const [votes, setVotes] = useState([]);

    if (!poll || !Array.isArray(poll.options)) return null;

    // Normalize options to be objects
    const normalizedOptions = poll.options.map((opt, index) =>
        typeof opt === "string" ? { id: index, caption: opt } : opt
    );

    useEffect(() => {
        if (poll && Array.isArray(poll.options)) {
            fetch(`/polls/${poll.id}/votes`)
                .then(res => res.json())
                .then(data => {
                    // Ensure votes array contains numbers, not objects
                    const voteCounts = Array.isArray(data)
                        ? data.map(v => (typeof v === "number" ? v : 0))
                        : Array(poll.options.length).fill(0);
                    setVotes(voteCounts);
                })
                .catch(() => setVotes(Array(poll.options.length).fill(0)));
        }
    }, [poll]);

    const handleVote = (optionId, index) => {
        fetch(`/votes`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                poll: { id: poll.id },
                votesOn: { id: optionId },
                voter: { id: 1 } // Replace with actual user ID
            })
        })
        .then(res => res.json())
        .then(data => {
            // Safely update votes to numbers
            const voteCounts = Array.isArray(data.votes)
                ? data.votes.map(v => (typeof v === "number" ? v : votes[index] + 1))
                : votes.map((v, i) => i === index ? v + 1 : v);
            setVotes(voteCounts);
        })
        .catch(() => {
            setVotes(votes.map((v, i) => i === index ? v + 1 : v));
        });
    };

    return (
        <div style={{ padding: "20px", fontFamily: "sans-serif" }}>
            <h2>{poll?.question ?? "Untitled poll"}</h2>
            <ul>
                {normalizedOptions.map((option, index) => (
                    <li key={option.id ?? index} style={{ margin: "8px 0" }}>
                        <button onClick={() => handleVote(option.id, index)}>
                            {option.caption ?? `Option ${index + 1}`}
                        </button>
                        <span style={{ marginLeft: "10px" }}>
                            Votes: {typeof votes[index] === "number" ? votes[index] : 0}
                        </span>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default Votepoll;
