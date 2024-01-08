import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import './changePassword.css';

function ChangePassword() {
    const [newPassword, setNewPassword] = useState('');
    const [message, setMessage] = useState('');
    const location = useLocation();
    const [dozvola, setDozvola] = useState(false);

    // Izvlačenje tokena iz URL-a
    const searchParams = new URLSearchParams(location.search);
    const token = searchParams.get('token');

    useEffect(() => {
        // Provjera valjanosti tokena
        fetch('/api/reset/promijeniLozinku', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `token=${token}`
        })
        .then(response => {
            var stariDiv = document.getElementsByClassName('alert-container')[0]; //u slucaju da je osoba vec fulala izbrisi stari div
            if (stariDiv && stariDiv.parentElement) {
                stariDiv.parentElement.removeChild(stariDiv);
            }
            if (response.status >= 300 && response.status < 600) {  //ako je doslo do greske
                response.json().then((data) => { //otpakiraj backendov odgovor i izvuci data
                    var noviDiv = document.createElement('div'); //ubaci div s tekstom greske
                    noviDiv.className = 'alert-container';
                    noviDiv.textContent = data.message;
                    var udiv = document.getElementsByClassName('change-password-form')[0]; //ovo prilagoditi ovom htmlu dole za svaku stranicu ce bit drukcije
                    udiv.insertBefore(noviDiv, udiv.firstElementChild);
                    setDozvola(false); //onemoguci slanje ovog zahtjeva ispod
                });
            }else{
                setDozvola(true);
            }})
        .catch(error => {
            console.error('Error:', error);
        });
    }, []);

    const handleSubmit = (e) => {
        e.preventDefault();
        if(!dozvola) return;
        // Slanje nove lozinke i tokena
        fetch('/api/reset/spremiLozinku', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `token=${token}&lozinka=${newPassword}` 
        })
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
                    noviDiv.textContent = data.message; //iz data izvuci poruku
                    var udiv = document.getElementsByClassName('change-password-form')[0]; //ovo prilagoditi ovom htmlu dole za svaku stranicu ce bit drukcije
                    udiv.insertBefore(noviDiv, udiv.firstElementChild);
                });
            } else { //ako nije doslo do greske
                setMessage("Uspješno ste promijenili svoju lozinku."); //ovo pokrece crtanje diva u liniji 80
                window.location.replace('/');
            }
        })
        .catch((error) => { //ako front uopce nije uspio poslat zahtjev backendu
            console.error('Error:', error);
        });
    };

    return (
        <div className="change-password-container">
            <h2>Promijeni Lozinku</h2>
            {message && <p>{message}</p>}
            <form className="change-password-form" onSubmit={handleSubmit}>
                <input
                    type="password"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                    placeholder="Unesite novu lozinku"
                    required
                />
                <button type="submit">Spremi novu lozinku</button>
            </form>
        </div>
    );
}

export default ChangePassword;