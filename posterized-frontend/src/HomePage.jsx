// HomePage.jsx
import React, { useState } from 'react';
import Header from './components/Header';
import axios from 'axios';

const HomePage = (props) => {
    const isLoggedIn = props.isLoggedIn;
    const onLogout = props.onLogout;

    const [pin, setPin] = useState('');
    const [pinMessage, setPinMessage] = useState('');

    const handlePinSubmit = async () => {
        try {
            console.log("Submitting PIN:", pin);
            const response = await axios.post("/api/pin", null, {
                params: { pin: pin },
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            setPinMessage(`Conference found: ${response.data.naziv}`);
        } catch (err) {
            setPinMessage('Conference not found.');
        }
    };

    return (
        <div className="page-container">
            <Header isLoggedIn={isLoggedIn} onLogout={onLogout} />
            <main className="main-content">
                <h1>Welcome to the Homepage</h1>
                {isLoggedIn && (
                    <div>
                        <label htmlFor="pinInput">Enter PIN:</label>
                        <input
                            type="text"
                            id="pinInput"
                            value={pin}
                            onChange={(e) => setPin(e.target.value)}
                        />
                        <button onClick={handlePinSubmit}>Check PIN</button>
                        {pinMessage && <p>{pinMessage}</p>}
                    </div>
                )}
                {/* Rest of the main content */}
            </main>
        </div>
    );
};

export default HomePage;
