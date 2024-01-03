
import axios from 'axios';
import { Link } from 'react-router-dom';
import './Register.css';
import ReCAPTCHA from 'react-google-recaptcha';
import {useState} from "react";

function Register() {
    const [ime, setIme] = useState('');
    const [prezime, setPrezime] = useState('');
    const [email, setEmail] = useState('');
    const [lozinka, setLozinka] = useState('');

    const [recaptchaValue, setRecaptchaValue] = useState(null);

    async function save(event) {
        event.preventDefault();
        if (!recaptchaValue) {
            alert('Molimo Vas riješite reCAPTCHA-u.');
            return;
        }
        var stariDiv = document.getElementsByClassName('alert-container')[0];
        if (stariDiv && stariDiv.parentElement) {
            stariDiv.parentElement.removeChild(stariDiv);
        }
        try {
            console.log("Submitting:", ime, prezime, email, lozinka);
            await axios.post("/api/registracija", {
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
            alert("Registracija uspješna.");
            window.location.replace("/");
        } catch (err) {
            var noviDiv = document.createElement('div');
            noviDiv.className = 'alert-container';
            noviDiv.textContent = err.response.data.message;
            var udiv = document.getElementsByClassName('register-container')[0];
            udiv.insertBefore(noviDiv, document.getElementById("moj"));
        }
    }

    return (
        <div className="centered-wrapper">
            <div className="register-container">
                <h2>Create a new account</h2>
                <form onSubmit={save} id="moj">
                    <div className="form-group">
                        <label htmlFor="ime">Ime:</label>
                        <input
                            type="text"
                            id="ime"
                            value={ime}
                            onChange={(event) => setIme(event.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="prezime">Prezime:</label>
                        <input
                            type="text"
                            id="prezime"
                            value={prezime}
                            onChange={(event) => setPrezime(event.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="email">Email:</label>
                        <input
                            type="email"
                            id="email"
                            value={email}
                            onChange={(event) => setEmail(event.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="lozinka">Lozinka:</label>
                        <input
                            type="password"
                            id="lozinka"
                            value={lozinka}
                            onChange={(event) => setLozinka(event.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <ReCAPTCHA
                            sitekey="6LfenzQpAAAAAHbcZlqaCK71MpSypvUydA3g4mMS"
                            onChange={(value) => setRecaptchaValue(value)}
                        />
                    </div>
                    <div className="form-group">
                        <Link to="/login">Već imate račun? Prijavite se!</Link>
                    </div>
                    <div className="form-group">
                        <button type="submit">Registracija</button>
                    </div>
                </form>
            </div>
        </div>
    );
}
export default Register;

