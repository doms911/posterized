// ConferenceInput.jsx
import React, { useState } from 'react';
import './login.css';

function AddConference() {

    const [pin, setPin] = useState('');
    const [adminEmail, setAdminEmail] = useState('');
   

    async function handleSubmit(e) {
        e.preventDefault();
        const body = `pin=${pin}&adminEmail=${adminEmail}`;
        const options = {
            credentials: 'include',
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: body,
        };
        fetch('/api/konferencija/stvoriKonf', options)
            .then((response) => {
                if (response.status === 401) {
                    alert('Dogodila se greška. Konferencija nije kreirana.');
                } else {
                    alert('Konferencija uspješno dodana');
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
                    <h2>Nova konferencija</h2>
                    <form onSubmit={handleSubmit}>
                        <div>
                            <label>Pin:</label>
                            <input
                                type="number"
                                id="pin"
                                value={pin}
                                onChange={(e) => setPin(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <label>Admin:</label>
                            <input
                                type="email"
                                id="adminEmail"
                                value={adminEmail}
                                onChange={(e) => setAdminEmail(e.target.value)}
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

export default AddConference;
