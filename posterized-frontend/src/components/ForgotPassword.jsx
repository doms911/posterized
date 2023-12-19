import React, { useState } from 'react';
import axios from 'axios';
import './ForgotPassword.css';

function ForgotPassword() {
    const [email, setEmail] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // Ovdje dodajte endpoint vašeg API-a za resetiranje lozinke
            const response = await axios.post('/api/reset-password', { email });
            setMessage('Ako e-mail postoji u našem sustavu, poslat ćemo vam upute za resetiranje lozinke.');
        } catch (error) {
            console.error('Došlo je do greške:', error);
            setMessage('Došlo je do greške. Molimo pokušajte ponovno.');
        }
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