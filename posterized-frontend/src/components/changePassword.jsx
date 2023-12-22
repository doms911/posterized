import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import './changePassword.css';


function ChangePassword() {
    const [newPassword, setNewPassword] = useState('');
    const [message, setMessage] = useState('');
    const { id, token } = useParams(); // Extract id and token from URL params
    const navigate = useNavigate();

    // Function to check token validity
    useEffect(() => {  //koristi se zato da se ne ucita body prije provjere tokena
        fetch('/api/reset/promijeniLozinku', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `token=${token}`
        })
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                setMessage(data.error);
            }
        })
        .catch(error => {
            setMessage('Došlo je do greške.');
        });
    }, [token]);

    // Function to handle form submission
    const handleSubmit = (e) => {
        e.preventDefault();
        fetch('/api/reset/spremiLozinku', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `token=${token}&password=${newPassword}`
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                setMessage('Vaša lozinka je uspješno promijenjena.');
                navigate('/'); // Redirect to home or other page
            } else {
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