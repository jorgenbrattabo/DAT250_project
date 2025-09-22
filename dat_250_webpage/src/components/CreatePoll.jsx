import { useState } from "react";

function CreatePoll({ onCreate }) {
    const [question, setQuestion] = useState("");
    const [options, setOptions] = useState([""]);
    const [message, setMessage] = useState("");

    const handleQuestionChange = (e) => setQuestion(e.target.value);

    const handleOptionChange = (index, value) => {
        const newOptions = [...options];
        newOptions[index] = value;
        setOptions(newOptions);
    };

    const addOption = () => setOptions([...options, ""]);

    const handleSubmit = (e) => {
        e.preventDefault();
        if (question && options.every(opt => opt.trim() !== "")) {
            fetch("/polls", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ question, options })
            })
            .then(res => {
                if (res.ok) {
                    return res.json();
                } else {
                    throw new Error("Error creating poll");
                }
            })
            .then(data => {
                // Render only the poll question, not the whole object
                setMessage(`Poll "${data.question}" created!`);
                
                if (onCreate) onCreate(data);

                setQuestion("");
                setOptions([""]);
            })
            .catch(() => {
                setMessage("Error creating poll.");
            });
        }
    };

    return (
        <form onSubmit={handleSubmit} style={{ marginBottom: "2rem", width: "320px" }}>
            <h2 style={{ fontWeight: "bold", textAlign: "center" }}>Create a Poll</h2>

            <div style={{ textAlign: "center", marginBottom: "1rem" }}>
                <label style={{ fontWeight: "bold", display: "block", marginBottom: "4px" }}>
                    Question:
                    <input
                        type="text"
                        value={question}
                        onChange={handleQuestionChange}
                        required
                        style={{ width: "100%" }}
                    />
                </label>
            </div>

            <div style={{ textAlign: "center" }}>
                <h4 style={{ fontWeight: "bold", marginBottom: "4px" }}>Options:</h4>
                {options.map((option, idx) => (
                    <div key={idx} style={{ marginBottom: "6px" }}>
                        <input
                            type="text"
                            value={option}
                            onChange={e => handleOptionChange(idx, e.target.value)}
                            required
                            style={{ width: "100%" }}
                        />
                    </div>
                ))}
                <button type="button" onClick={addOption}>Add Option</button>
            </div>

            <button type="submit" style={{ marginTop: "10px" }}>Create Poll</button>

            {/* Render only strings */}
            {message && <div style={{ marginTop: "10px" }}>{message}</div>}
        </form>
    );
}

export default CreatePoll;