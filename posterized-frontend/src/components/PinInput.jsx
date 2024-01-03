//PinInput.jsx
import React, { useState } from 'react';
import Header from './Header';
import axios from 'axios';
import "./login.css"

const PinInput = (props) => {
    const isLoggedIn = props.isLoggedIn;
    const onLogout = props.onLogout;

    const [pin, setPin] = useState('');
    const [pinMessage, setPinMessage] = useState('');

    const handlePinSubmit = async () => {
        try {
            console.log("Submitting PIN:", pin);
            const response = await axios.post("/api/konferencija/pin", null, {
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
        <div className="page-container" >
            <Header isLoggedIn={isLoggedIn} onLogout={onLogout} />

            <div className="centered-wrapper">
            <div className="container"> 
            <h2>Pristupite konferenciji</h2>
            <div>         
                <label htmlFor="pinInput">Unesite PIN:</label>
                <input
                    type="text"
                    id="pinInput"
                    value={pin}
                    onChange={(e) => setPin(e.target.value)}
                    />
                <button name="dodaj" onClick={handlePinSubmit}>Provjeri PIN</button>
                {pinMessage && <p>{pinMessage}</p>}
                </div>
            </div>  
            </div>
        </div>
    );
};

export default PinInput;
