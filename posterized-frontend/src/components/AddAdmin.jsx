//AddAdmin.jsx
import Header from './Header';
import axios from 'axios';
import './login.css';
import {useState} from "react";

function AddAdmin(props) {

    const isLoggedIn = props.isLoggedIn;
    const onLogout = props.onLogout;
    const [ime, setIme] = useState('');
    const [prezime, setPrezime] = useState('');
    const [email, setEmail] = useState('');
    const [lozinka, setLozinka] = useState('');

    async function save(event) {
        event.preventDefault();
   
        var stariDiv = document.getElementsByClassName('alert-container')[0];
        if (stariDiv && stariDiv.parentElement) {
            stariDiv.parentElement.removeChild(stariDiv);
        }
        try {
            console.log("Submitting:", ime, prezime, email, lozinka);
            await axios.post("/api/registracija/admin", {
                ime: ime,
                prezime: prezime,
                email: email,
                lozinka: lozinka,
                uloga: "admin"
            }, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            alert("Admin je uspješno dodan!");
            setIme('');
            setPrezime('');
            setEmail('');
            setLozinka('')
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
            <Header isLoggedIn={isLoggedIn} onLogout={onLogout} />
            <div className="register-container">
                <h2>Dodavanje admina</h2>
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
                      
                    </div>
                    <div className="form-group">
                        <button type="submit">Dodaj</button>
                    </div>
                </form>
            </div>
        </div>
    );
}
export default AddAdmin;

