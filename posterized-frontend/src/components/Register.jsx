    import React, { useState } from 'react';
    import axios from 'axios';
    
    function Register() {
        const [ime, setIme] = useState('');
        const [prezime, setPrezime] = useState('');
        const [email, setEmail] = useState('');
        const [lozinka, setLozinka] = useState('');
    
            async function save(event) {
                event.preventDefault();
                try{
                    console.log("Submitting:", ime, prezime, email, lozinka);
                    await  axios.post("http://localhost:8080/registracija", {
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
                } catch(err) {
                    console.error(err);
                    if(err.response.data.message) {
                        alert(err.response.data.message)
                    } else alert("An error occurred. Please check the console for details.");
                }
        }
    
        return (
            <div>
                <h2>Register Page</h2>
                <form onSubmit={save}>
                    <div>
                        <label>Ime:</label>
                        <input
                            type="ime"
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
                            type="prezime"
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
                            type="lozinka"
                            value={lozinka}
                            onChange={(event) => {
                                setLozinka(event.target.value);
                            }}
                            required
                        />
                    </div>
                    <button type="submit">Register</button>
                </form>
    
            </div>
        );
    }
    
    export default Register;