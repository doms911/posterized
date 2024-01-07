// Login.jsx
import React, { useState } from 'react';
import Cookies from 'js-cookie';
import './login.css';
import ReCAPTCHA from 'react-google-recaptcha';

function Login(props) {
    const onLogin = props.onLogin;
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [recaptchaValue, setRecaptchaValue] = useState(null);

    async function authenticate(e) {
        e.preventDefault();

        if (!recaptchaValue) {
            alert('Molimo Vas riješite reCAPTCHA-u.');
            return;
        }

        const body = `username=${username.toLowerCase()}&password=${password}`;
        const options = {
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: body,
        };
        fetch('/api/login', options)
            .then((response) => {
                var stariDiv = document.getElementsByClassName('alert-container')[0];
                if (stariDiv && stariDiv.parentElement) {
                    stariDiv.parentElement.removeChild(stariDiv);
                }
                if (response.status >= 300 && response.status < 600) {
                    response.json().then((data) => {
                        var noviDiv = document.createElement('div');
                        noviDiv.className = 'alert-container';
                        const poruka = data.message === "Bad credentials" ? "Pogrešni podatci" : data.message;
                        noviDiv.textContent = poruka;
                        var udiv = document.getElementsByClassName('container')[0];
                        udiv.insertBefore(noviDiv, document.getElementById("moj"));
                    });
                } else {
                    const userRole = response.headers.get('X-Role');
                    Cookies.set('user', 'authenticated'); 
                    localStorage.setItem('username', username.toLowerCase());
                    localStorage.setItem('name', response.headers.get('X-Name'));
                    localStorage.setItem('userRole', userRole);
                    alert('Prijava uspješna.');
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
                    <h2>Prijava</h2>
                    <form onSubmit={authenticate} id="moj">
                        <div >
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
                            <label>Lozinka:</label>
                            <input
                                type="password"
                                id="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <a href="/forgot-password">Zaboravili ste lozinku?</a>
                        </div>
                        <ReCAPTCHA
                            sitekey="6LfenzQpAAAAAHbcZlqaCK71MpSypvUydA3g4mMS"
                            onChange={(value) => setRecaptchaValue(value)}
                        />
                        <div>
                            <button name="prijava" type="submit">Prijavi se</button>
                        </div>
                    </form>
                </div>
            </div>
    );
}

export default Login;
