// AddAuthor.jsx
import React, { useState } from 'react';
import './login.css';

function AddAuthor(props) {

    const [email, setEmail] = useState('');
    const [name, setName] = useState('');
    const [surname, setSurname] = useState('');

    async function handleSubmit(e) {
        e.preventDefault();
        const body = `email=${email.toLowerCase()}&name=${name}&surname=${surname}`;
        const options = {
            credentials: 'include',
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: body,
        };
        fetch('/api/addAuthor', options)
            .then((response) => {
                if (response.status === 401) {
                    alert('Dogodila se greška. Autor nije dodan.');
                } else {
                    alert('Autor uspješno dodan');
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
                    <h2>Dodavanje autora</h2>
                    <form onSubmit={handleSubmit}>
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
