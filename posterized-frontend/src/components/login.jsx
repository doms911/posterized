
import React, { useState } from 'react';
import axios from 'axios';

function Login() {
    const [email, setEmail] = useState('');
    const [lozinka, setLozinka] = useState('');

    async function authenticate(event) {
        event.preventDefault();
        try {
            console.log("Logging in with:", email, lozinka);
        } catch (error) {
            console.error("Login error:", error);
        }
    }

    return (
        <div>
            <h2>Login Page</h2>
            <form onSubmit={authenticate}>
                <div>
                    <label htmlFor="email">Email:</label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </div>
                <div>
                    <label htmlFor="lozinka">Lozinka:</label>
                    <input
                        type="password"
                        id="lozinka"
                        value={lozinka}
                        onChange={(e) => setLozinka(e.target.value)}
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
