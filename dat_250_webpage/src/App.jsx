import { useState } from "react";
import './App.css';
import CreatePoll from "./components/CreatePoll";
import CreateUser from "./components/CreateUser";
import Votepoll from "./components/Votepoll";


function App() {
    const [poll, setPoll] = useState(null);
    
    const handleCreatePoll = (newPoll) => {
        console.log("Poll received:", newPoll);
        setPoll(newPoll);
    };

    return (
        <div 
            style={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
                minHeight: "100vh"
            }}>
            <h1>Poll App</h1>
            <CreateUser />
            <CreatePoll onCreate={handleCreatePoll} />
            {poll && <Votepoll poll={poll} />}
        </div>
    );
}

export default App;