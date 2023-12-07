// Login.jsx
import React, { useState } from 'react';
import Cookies from 'js-cookie';
import './login.css';

function Login(props) {
    const onLogin = props.onLogin;

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    async function authenticate(e) {
        e.preventDefault();
        const body = `username=${username.toLowerCase()}&password=${password}`;
        const options = {
            credentials: 'include',
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: body,
        };
        fetch('/api/login', options)
            .then((response) => {
                if (response.status === 401) {
                    alert('Pogrešna lozinka');
                } else {
                    alert('Prijava uspješna');
                    Cookies.set('user', 'authenticated'); // Set cookie to expire in 7 days
                    localStorage.setItem('username', username.toLowerCase());
                    onLogin();
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
                    <h2>Log in</h2>
                    <form onSubmit={authenticate}>
                        <div>
                            <label>Email:</label>
                            <input
                                type="text"
                                id="username"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <label>Password:</label>
                            <input
                                type="password"
                                id="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <a href="">Forgot your password?</a>
                        </div>
                        <div>
                            <button name="prijava" type="submit">Prijavi se</button>
                        </div>
                    </form>
                </div>
            </div>
    );
}

export default Login;
