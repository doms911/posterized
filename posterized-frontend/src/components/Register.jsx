    import React, { useState } from 'react';
    import axios from 'axios';
    import { Link } from 'react-router-dom';
    import './Register.css';

    function Register() {
        const [ime, setIme] = useState('');
        const [prezime, setPrezime] = useState('');
        const [email, setEmail] = useState('');
        const [lozinka, setLozinka] = useState('');
    
            async function save(event) {
                event.preventDefault();
                try{
                    console.log("Submitting:", ime, prezime, email, lozinka);
                    await  axios.post("/api/registracija", {
                        ime: ime,
                        prezime: prezime,
                        email: email,
                        lozinka: lozinka,
                        uloga: "korisnik"
                    }, {
                        headers: {
                            'Content-Type': 'application/json',
                        },
                    });
                    alert("Registracija uspješna");
                    window.location.replace("/");
                } catch(err) {
                    console.error(err);
                    if(err.response.data.message) {
                        alert(err.response.data.message)
                    } else alert("An error occurred. Please check the console for details.");
                }
        }
    
        return (
            <div className="centered-wrapper">
                <div className="register-container">
                    <h2>Create a new account</h2>
                    <form onSubmit={save}>
                        <div>
                            <label>Ime:</label>
                            <input
                                type="text"
                                value={ime}
                                onChange={(event) => {
                                    setIme(event.target.value);
                                }}
                                required
                            />
                        </div>
                        <div>
                            <label>Prezime:</label>
                            <input
                                type="text"
                                value={prezime}
                                onChange={(event) => {
                                    setPrezime(event.target.value);
                                }}
                                required
                            />
                        </div>
                        <div>
                            <label>Email:</label>
                            <input
                                type="email"
                                value={email}
                                onChange={(event) => {
                                    setEmail(event.target.value);
                                }}
                                required
                            />
                        </div>
                        <div>
                            <label>Password:</label>
                            <input
                                type="password"
                                value={lozinka}
                                onChange={(event) => {
                                    setLozinka(event.target.value);
                                }}
                                required
                            />
                        </div>
                        <Link to="/login">
                            <div>Log in</div>
                        </Link>
                        <button type="submit">Register</button>
                    </form>

                </div>
            </div>
        );
    }
    
    export default Register;