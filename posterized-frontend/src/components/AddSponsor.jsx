//AddSponsor.jsx
import React, { useState } from 'react';
import Header from './Header';
import axios from 'axios';
import './login.css';
import qs from 'qs';

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
  
    const formData = new FormData();
    formData.append('naziv', naziv);
    formData.append('url', url);
    formData.append('logo', logo);

    fetch('/api/pokrovitelj', {
        method: 'POST',
        body: formData
    }).then(response => {
      var stariDiv = document.getElementsByClassName('alert-container')[0];
      if (stariDiv && stariDiv.parentElement) {
          stariDiv.parentElement.removeChild(stariDiv);
      }
      if (response.status >= 300 && response.status < 600) {
          response.json().then((data) => {
              var noviDiv = document.createElement('div');
              noviDiv.className = 'alert-container';
              noviDiv.textContent = data.message;
              var udiv = document.getElementById('moj');
              udiv.insertBefore(noviDiv, udiv.firstElementChild);
          });
      }else alert("Sponzor je uspješno dodan!");}
      )
  .catch(error => {
      console.error('Error:', error);
  });
  }

  return (
    <div className="centered-wrapper">
      <Header isLoggedIn={isLoggedIn} onLogout={onLogout} />
      <div className="container">
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
