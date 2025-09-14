import { useState } from "react";

function CreateUser() {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [message, setMessage] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();
        fetch("/users", {
            method: "POST",
            headers: {"Content-Type": "application/json" },
            body: JSON.stringify({ username, email })

        })
        .then(res => {
            if (res.ok) {
            setMessage(`Welcome ${username}!`);
            setUsername("");
            setEmail("");
        } else {
            setMessage("Error creating user.");
        }
    })
    .catch(() => setMessage("Network error."))
};

return (
    <form onSubmit={handleSubmit} style={{ marginBottom: "2rem", width: "320px" }}>
        <h2 style={{ fontWeight: "bold", textAlign: "center" }}>Create User</h2>
        <div style={{ marginBottom: "1rem" }}>
                <label style={{ fontWeight: "bold", display: "block", marginBottom: "4px" }}>
                    Username:
                </label>
                <input
                    type="text"
                    value={username}
                    onChange={e => setUsername(e.target.value)}
                    required
                    style={{ width: "100%" }}
                />
        </div>
            <div style={{ marginBottom: "1rem" }}>
                <label style={{ fontWeight: "bold", display: "block", marginBottom: "4px" }}>
                    Email:
                </label>
                <input
                    type="email"
                    value={email}
                    onChange={e => setEmail(e.target.value)}
                    required
                    style={{ width: "100%" }}
                />
            </div>
            <button type="submit" style={{ marginTop: "10px" }}>Create User</button>
            {message && <div style={{ marginTop: "10px" }}>{message}</div>}
        </form>
    );
}

export default CreateUser;