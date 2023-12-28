import React, { useState } from 'react';
import './ForgotPassword.css';

function ForgotPassword() {
    const [email, setEmail] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();

        const body = `email=${email.toLowerCase()}`;
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: body,
        };
        fetch('/api/reset/resetLozinka', options)
            .then((response) => { //front je uspio poslat zahtjev backendu
                var stariDiv = document.getElementsByClassName('alert-container')[0]; //u slucaju da je osoba vec fulala izbrisi stari div
                if (stariDiv && stariDiv.parentElement) {
                    stariDiv.parentElement.removeChild(stariDiv);
                }
                if(message)setMessage(''); //ako je za prosli zahtjev bila postavljena poruka kad je bilo uspjesno sad i nju izbrisi, ovo mozda inaca nece trebat ak nema ispisa za uspjesno
                if (response.status >= 300 && response.status < 600) {  //ako je doslo do greske
                    response.json().then((data) => { //otpakiraj backendov odgovor i izvuci data
                        var noviDiv = document.createElement('div'); //ubaci div za tekst greske
                        noviDiv.className = 'alert-container';
                        noviDiv.textContent = data.message;//iz data izvuci poruku
                        var udiv = document.getElementsByClassName('form-group')[0]; //ovo prilagoditi ovom htmlu dole za svaku stranicu ce bit drukcije
                        udiv.insertBefore(noviDiv, udiv.firstElementChild);
                    });
                } else { //ako nije doslo do greske
                    setMessage("An email with instructions has been successfully sent to you."); //ovo pokrece crtanje diva u liniji 60
                }
            })
            .catch((error) => { //ako front uopce nije uspio poslat zahtjev backendu
                console.error('Error:', error);
            });
    };

    return (
        <div className="forgot-password-container">
            <h2>Resetiranje Lozinke</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="email">E-mail:</label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Pošalji zahtjev</button>
            </form>
            {message && <div className="message">{message}</div>}
        </div>
    );
}

export default ForgotPassword;