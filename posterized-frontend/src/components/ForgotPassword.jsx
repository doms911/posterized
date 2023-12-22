import React, { useState } from 'react';
import './ForgotPassword.css';

function ForgotPassword() {
    const [email, setEmail] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();

        const body = `email=${encodeURIComponent(email)}`;
        const options = {
            method: 'POST',
            headers: {         'Content-Type': 'application/x-www-form-urlencoded',
        },
            body: body,
        };

        fetch('/api/reset/resetLozinka', options)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                setMessage('Ako e-mail postoji u našem sustavu, poslat ćemo vam upute za resetiranje lozinke.');
            })
            .catch(error => {
                console.error('Došlo je do greške:', error);
                setMessage('Došlo je do greške. Molimo pokušajte ponovno.');
            });
    };

    return (
        <div className="forgot-password-container">
            <h2>Resetiranje Lozinke</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="email">E-mail:</label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Pošalji zahtjev</button>
            </form>
            {message && <div className="message">{message}</div>}
        </div>
    );
}

export default ForgotPassword;