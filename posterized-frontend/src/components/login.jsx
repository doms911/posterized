
import React, { useState } from 'react';

function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    async function authenticate(e) {
        e.preventDefault();
        const body = `username=${username}&password=${password}`;
        const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: body
        };
        fetch('http://localhost:8080/login', options)
        .then(response => {
            if (response.status === 401) {
            alert("Pogrešna lozinka")
            } else {
            alert("Prijava uspješna");
            }
      });
    }

    return (
        <div>
            <h2>Login Page</h2>
            <form onSubmit={authenticate}>
                <div>
                    <label>Email:</label>
                    <input
                        type="username"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </div>
                <div>
                    <label>Password:</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <div>
                    <button type="submit">Prijavi se</button>
                </div>
            </form>
        </div>
    );
}

export default Login;
