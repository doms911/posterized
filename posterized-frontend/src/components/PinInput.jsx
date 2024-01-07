//PinInput.jsx
import React, { useState } from 'react';
import Header from './Header';
import axios from 'axios';
import "./PinInput.css"

const PinInput = (props) => {
    const isLoggedIn = props.isLoggedIn;
    const onLogout = props.onLogout;

    const [pin, setPin] = useState('');
    const [pinMessage, setPinMessage] = useState('');
    const [conferenceInfo, setConferenceInfo] = useState(null);


    const handlePinSubmit = async () => {
        try {
            console.log("Submitting PIN:", pin);
            const response = await axios.get(`/api/prisutan/${pin}`, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            setPinMessage(`Conference found: ${response.data[0].naziv}`);
            setConferenceInfo(response.data);
            console.log("Response Data:", response.data); // Logovanje celog odgovora
        } catch (err) {
           alert(err.response.data.message);
            setConferenceInfo(null);
        }
    };

    const isConferenceFinished = () => {
        if (conferenceInfo.length > 1) {
            return true;
        }
        return false;
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
                {conferenceInfo && (
                    <div>
                        <h3>Konferencija: {conferenceInfo[0].naziv}</h3>
                        <p>Mjesto: {conferenceInfo[0].mjesto}</p>
                        <p>Adresa: {conferenceInfo[0].adresa}</p>
                        <p>pbr: {conferenceInfo[0].pbr}</p>
                        <p>Admin: {conferenceInfo[0].admin}</p>
                        <p>Vrijeme početka: {conferenceInfo[0].vrijemePocetka}</p>
                        <p>Vrijeme kraja: {conferenceInfo[0].vrijemeKraja}</p>
                        {isConferenceFinished() ? (
                            <div>
                                <p>Konferencija je završena.</p>
                                {/* Dodatne informacije za završenu konferenciju */}
                            </div>
                        ) : (
                            <div>
                                <p>Konferencija je u toku.</p>
                                {/* Informacije relevantne za konferenciju koja je u toku */}
                            </div>
                        )}
                    </div>
                )}
            </div>
            </div>
        </div>

    );
};

export default PinInput;
