import React, { useState, useEffect } from 'react';
import Header from './Header';
import './login.css';

function ConferenceInput(props) {
  const isLoggedIn = props.isLoggedIn;
  const onLogout = props.onLogout;
  const [naziv, setNaziv] = useState('');
  const [pin, setPin] = useState('');
  const [videoURL, setVideoURL] = useState('');
  const [startTime, setStartTime] = useState('');
  const [endTime, setEndTime] = useState('');
  const [pbr, setPbr] = useState(0);
  const [sponsors, setSponsors] = useState([]);
  const [selectedSponsors, setSelectedSponsors] = useState([]);
  const [adresa, setAdresa] = useState('');
  const [mjesto, setMjesto] = useState('');


  useEffect(() => {
    // Dohvati prethodne podatke
    const fetchData = async () => {
      try {
        const response = await fetch('/api/konferencija/prikaziAdminuKonf/' + props.imeKonferencije, {
          credentials: 'include',
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        });

        if (!response.ok) {
          throw new Error(`Server error: ${response.status}`);
        }

        const data = await response.json();
        setNaziv(data[0].naziv);
        setPin(data[0].pin);
        setPbr(data[0].pbr);
        setAdresa(data[0].adresa);
        setMjesto(data[0].mjesto);
        setVideoURL(data[0].videoURL);
        setStartTime(data[0].startTime);
        setEndTime(data[0].endTime);

        // Dohvati sponzore
        const sponsorsResponse = await fetch('/api/pokrovitelj', {
          credentials: 'include',
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        });

        if (!sponsorsResponse.ok) {
          throw new Error(`Server error: ${sponsorsResponse.status}`);
        }

        const sponsorsData = await sponsorsResponse.json();
        setSponsors(sponsorsData);
      } catch (err) {
        console.error(err.response ? err.response.data.message : 'Nepoznata greška');
      }
    };

    fetchData();
  }, []);

  async function handleSubmit(e) {
    e.preventDefault();

    // Postavite svoju logiku za slanje podataka na server
    const body = `urlVideo=${videoURL}&vrijemePocetka=${startTime}&vrijemeKraja=${endTime}&mjesto=${mjesto}&pbr=${pbr}&adresa=${adresa}&sponzori=${selectedSponsors.join(',')}`;
    const options = {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: body,
    };

    try {
      const response = await fetch('/api/konferencija/nadopuniKonf/' + props.imeKonferencije, options);

      if (!response.ok) {
        throw new Error(`Server error: ${response.status}`);
      }
      alert('Podaci uspješno dodani');
    } catch (error) {
        console.error(error.response ? error.response.data.message : 'Nepoznata greška');
    }
  }

  return (
    <div className="centered-wrapper">
      <div className="container">
        <h2>Unos podataka o konferenciji</h2>
        <form onSubmit={handleSubmit}>
        <div>
  <label>Naziv:</label>
  <input
    type="text"
    id="naziv"
    value={naziv || ''}  // Dodajte "|| ''" kako biste spriječili undefined
    onChange={(e) => setNaziv(e.target.value)}
  />
</div>
          <div>
            <label>Pin:</label>
            {pin}
          </div>
          <div>
            <label>Video URL:</label>
            <input
              type="url"
              id="videoURL"
              value={videoURL || ''}
              onChange={(e) => setVideoURL(e.target.value)}
            />
          </div>
          <div>
            <label>Početak konferencije:</label>
            <input
              type="datetime-local"
              id="startTime"
              value={startTime || ''}
              onChange={(e) => setStartTime(e.target.value)}
            />
          </div>
          <div>
            <label>Završetak konferencije:</label>
            <input
              type="datetime-local"
              id="endTime"
              value={endTime || ''}
              onChange={(e) => setEndTime(e.target.value)}
            />
          </div>
          <div>
            <label>Poštanski broj:</label>
            <input
              type="number"
              id="pbr"
              value={pbr || ''}
              onChange={(e) => setPbr(e.target.value)}
            />
          </div>
          <div>
            <label>Mjesto:</label>
            <input
              type="text"
              id="mjesto"
              value={mjesto || ''}
              onChange={(e) => setMjesto(e.target.value)}
            />
          </div>
          <div>
            <label>Adresa:</label>
            <input
              type="text"
              id="adresa"
              value={adresa || ''}
              onChange={(e) => setAdresa(e.target.value)}
            />
          </div>
          <div>
            <label>Sponzori:</label>
            <select
              multiple
              value={selectedSponsors}
              onChange={(e) => setSelectedSponsors(Array.from(e.target.selectedOptions, (option) => option.value))}
            >
              {sponsors.map((sponsor) => (
                <option key={sponsor} value={sponsor}>
                  {sponsor}
                </option>
              ))}
            </select>
          </div>
          <div>
            <button name="dodaj" type="submit">
              Dodaj
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default ConferenceInput;
