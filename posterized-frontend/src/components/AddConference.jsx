//AddConference.jsx
import React, { useState } from 'react';
import './login.css';
import Header from './Header';

function AddConference(props) {

    const isLoggedIn = props.isLoggedIn;
    const onLogout = props.onLogout;
    const [pin, setPin] = useState('');
    const [adminEmail, setAdminEmail] = useState('');
    const [naziv, setnaziv] = useState('');

   

    async function handleSubmit(e) {
        e.preventDefault();
        const body = `pin=${pin}&adminEmail=${adminEmail}&naziv=${naziv}`;
        const options = {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: body,
        };
        fetch('/api/konferencija/stvoriKonf', options)
        .then((response) => {
            var stariDiv = document.getElementsByClassName('alert-container')[0];
            if (stariDiv && stariDiv.parentElement) {
                stariDiv.parentElement.removeChild(stariDiv);
            }
            if (response.status >= 300 && response.status < 600) {
             response.json().then((data) => {
                    var noviDiv = document.createElement('div');
                    noviDiv.className = 'alert-container';
                    noviDiv.textContent = data.message;
                    var udiv = document.getElementsByClassName('container')[0];
                    udiv.insertBefore(noviDiv, document.getElementById("moj"));
                });
                } else {
                    alert('Konferencija uspješno dodana');
                }
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    }

    return (
        <div className="centered-wrapper">
            <Header isLoggedIn={isLoggedIn} onLogout={onLogout} />
            <div className="container">
                    <h2>Nova konferencija</h2>
                    <form onSubmit={handleSubmit} id="moj">
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
                            <label>Email admina:</label>
                            <input
                                type="email"
                                id="adminEmail"
                                value={adminEmail}
                                onChange={(e) => setAdminEmail(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <label>Naziv:</label>
                            <input
                                type="text"
                                id="naziv"
                                value={naziv}
                                onChange={(e) => setnaziv(e.target.value)}
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
