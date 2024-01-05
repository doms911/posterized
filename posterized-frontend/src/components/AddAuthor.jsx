// AddAuthor.jsx
import React, { useState } from 'react';
import './login.css';
import Header from './Header';

function AddAuthor(props) {

    const isLoggedIn = props.isLoggedIn;
    const onLogout = props.onLogout;
    const [email, setEmail] = useState('');
    const [name, setName] = useState('');
    const [surname, setSurname] = useState('');

    async function handleSubmit(e) {
        e.preventDefault();
        const body = `email=${email.toLowerCase()}&name=${name}&surname=${surname}`;
        const options = {
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: body,
        };
        fetch('/api/addAuthor', options)
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
                    alert('Autor uspješno dodan');
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
                    <h2>Dodavanje autora</h2>
                    <form onSubmit={handleSubmit} id="moj">
                        <div>
                            <label>Ime:</label>
                            <input
                                type="text"
                                id="name"
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <label>Prezime:</label>
                            <input
                                type="text"
                                id="surname"
                                value={surname}
                                onChange={(e) => setSurname(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <label>Email:</label>
                            <input
                                type="email"
                                id="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
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

export default AddAuthor;
