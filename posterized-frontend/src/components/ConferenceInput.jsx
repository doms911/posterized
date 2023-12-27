// ConferenceInput.jsx
import React, { useState } from 'react';
import './login.css';

function ConferenceInput(props) {

    const [videoURL, setVideoURL] = useState('');
    const [startTime, setStartTime] = useState('');
    const [endTime, setEndTime] = useState('');
    const [pbr, setPbr] = useState(0);

    async function handleSubmit(e) {
        e.preventDefault();
        const body = `videoURL=${videoURL.toLowerCase()}&startTime=${startTime}&endTime=${endTime}&pbr=${pbr}`;
        const options = {
            credentials: 'include',
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: body,
        };
        fetch('/api/konferencija/nadopuniKonf', options)
            .then((response) => {
                if (response.status === 401) {
                    alert('Dogodila se greška. Podaci nisu uneseni.');
                } else {
                    alert('Podaci uspješno dodani');
                    window.location.replace('/');
                }
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    }

    return (
        <div className="centered-wrapper">
            <div className="container">
                    <h2>Unos podataka o konferenciji</h2>
                    <form onSubmit={handleSubmit}>
                        <div>
                            <label>Video URL:</label>
                            <input
                                type="text"
                                id="videoURL"
                                value={videoURL}
                                onChange={(e) => setVideoURL(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <label>Početak konferencije:</label>
                            <input
                                type="datetime-local"
                                id="startTime"
                                value={startTime}
                                onChange={(e) => setStartTime(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <label>Završetak konferencije:</label>
                            <input
                                type="datetime-local"
                                id="endTime"
                                value={endTime}
                                onChange={(e) => setEndTime(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <label>Poštanski broj:</label>
                            <input
                                type="number"
                                id="pbr"
                                value={pbr}
                                onChange={(e) => setPbr(e.target.value)}
                                required
                            />
                        </div>
                        
                        <div>
                            <button name="dodaj" type="submit">Dodaj</button>
                        </div>
                    </form>
                </div>
            </div>
    );
}

export default ConferenceInput;
