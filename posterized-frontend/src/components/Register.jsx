
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
    const [showAlert, setShowAlert] = useState(false);
    const [alertMessage, setAlertMessage] = useState('');

    const [recaptchaValue, setRecaptchaValue] = useState(null);

    async function save(event) {
        event.preventDefault();
        if (!recaptchaValue) {
            alert('Please complete the reCAPTCHA.');
            return;
        }
        var stariDiv = document.getElementsByClassName('alert-container1');
        if (stariDiv && stariDiv.parentNode) {
            stariDiv.parentNode.removeChild(stariDiv);
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
            alert("Registracija uspješna");
            window.location.replace("/");
        } catch (err) {
            setShowAlert(true);
            setAlertMessage(err.response.data.message);
        }
    }

    return (
        <div className="centered-wrapper">
            <div className="register-container">
                <h2>Create a new account</h2>
                {showAlert && (
                        <div className="alert-container">
                            {alertMessage}
                        </div>
                    )}
                <form onSubmit={save}>
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
                        <label htmlFor="lozinka">Password:</label>
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
                        <Link to="/login">Already have an account? Log in</Link>
                    </div>
                    <div className="form-group">
                        <button type="submit">Register</button>
                    </div>
                </form>
            </div>
        </div>
    );
}
export default Register;

