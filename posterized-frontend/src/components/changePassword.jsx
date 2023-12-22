import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import './changePassword.css';

function ChangePassword() {
    const [newPassword, setNewPassword] = useState('');
    const [message, setMessage] = useState('');
    const location = useLocation();
    const navigate = useNavigate();

    // Izvlačenje tokena iz URL-a
    const searchParams = new URLSearchParams(location.search);
    const token = searchParams.get('token');

    useEffect(() => {
        // Provjera valjanosti tokena
        fetch('/api/reset/promijeniLozinku', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `token=${token}`
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Greška u zahtjevu: ' + response.statusText);
            }
            if(response.status === 204 || response.status === 205 || response.bodyUsed === false) {
                //setMessage('Token je provjeren i valjan.');
                return null;
            }
            return response.json();
        })
        .then(data => {
            if (data && data.error) {
                setMessage(data.error);
            }
        })
        .catch(error => {
            setMessage('Došlo je do greške.');
        });
    }, [token]);

    const handleSubmit = (e) => {
        e.preventDefault();
        // Slanje nove lozinke i tokena
        fetch('/api/reset/spremiLozinku', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `token=${token}&password=${newPassword}`
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Greška u zahtjevu: ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            if (data && data.success) {
                setMessage('Vaša lozinka je uspješno promijenjena.');
                navigate('/'); // Redirect na početnu stranicu
            } else if (data && data.error) {
                setMessage(data.error);
            }
        })
        .catch(error => {
            setMessage('Došlo je do greške pri promjeni lozinke.');
        });
    };

    return (
        <div className="change-password-container">
            <h2>Promijeni Lozinku</h2>
            {message && <p>{message}</p>}
            <form className="change-password-form" onSubmit={handleSubmit}>
                <input
                    type="password"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                    placeholder="Unesite novu lozinku"
                    required
                />
                <button type="submit">Spremi novu lozinku</button>
            </form>
        </div>
    );
}

export default ChangePassword;