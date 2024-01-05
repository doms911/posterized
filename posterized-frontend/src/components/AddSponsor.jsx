//AddSponsor.jsx
import React, { useState } from 'react';
import Header from './Header';
import axios from 'axios';
import './login.css';

function AddSponsor(props) {
  const isLoggedIn = props.isLoggedIn;
  const onLogout = props.onLogout;
  const [naziv, setNaziv] = useState('');
  const [url, setUrl] = useState('');
  const [logo, setLogo] = useState(null);

  async function save(event) {
    event.preventDefault();
  
    // Uklonite postojeći div za poruke
    var stariDiv = document.getElementsByClassName('alert-container')[0];
    if (stariDiv && stariDiv.parentElement) {
      stariDiv.parentElement.removeChild(stariDiv);
    }
  
    try {
      // Koristite ispravan redoslijed argumenata, prvo URL, a zatim podaci
      await axios.post('/api/pokrovitelj', {
        naziv: naziv,
        url: url,
        logo: logo,
      }, {
        headers: {
          'Content-Type': 'application/json',
        },
      });
      alert("Sponzor je uspješno dodan!");
    } catch (err) {
      // Kreirajte novi div za prikaz poruke o grešci
      var noviDiv = document.createElement('div');
      noviDiv.className = 'alert-container';
      noviDiv.textContent = err.response.data.message;
      
      // Dodajte novi div na odgovarajuće mjesto
      var udiv = document.getElementsByClassName('register-container')[0];
      udiv.insertBefore(noviDiv, document.getElementById("moj"));
    }
  }

  return (
    <div className="centered-wrapper">
      <Header isLoggedIn={isLoggedIn} onLogout={onLogout} />
      <div className="register-container">
        <h2>Dodavanje sponzora</h2>
        <form onSubmit={save} id="moj">
          <div className="form-group">
            <label htmlFor="naziv">Naziv:</label>
            <input
              type="text"
              id="naziv"
              value={naziv}
              onChange={(event) => setNaziv(event.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="url">Url:</label>
            <input
              type="url"
              id="url"
              value={url}
              onChange={(event) => setUrl(event.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="logo">Logo:</label>
            <input
              type="file"
              accept=".jpg, .jpeg, .png"
              id="logo"
              onChange={(event) => setLogo(event.target.files[0])}
              required
            />
          </div>
          <div className="form-group">
            <button type="submit">Dodaj</button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default AddSponsor;
