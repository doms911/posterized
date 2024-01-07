import React, { useState, useEffect } from 'react';
import './login.css';

function ConferenceInput(props) {
  const isLoggedIn = props.isLoggedIn;
  const onLogout = props.onLogout;
  const [naziv, setNaziv] = useState('');
  const [pin, setPin] = useState('');
  const [videoURL, setVideoURL] = useState('');
  var [startTime, setStartTime] = useState('');
  var [endTime, setEndTime] = useState('');
  const [pbr, setPbr] = useState(0);
  const [sponsors, setSponsors] = useState([]);
  const [selectedSponsors, setSelectedSponsors] = useState([]);
  const [adresa, setAdresa] = useState('');
  const [mjesto, setMjesto] = useState('');
  


  useEffect(() => {
    // Dohvati prethodne podatke
    const fetchData = async () => {
      try {
        fetch('/api/konferencija/prikaziAdminuKonf/' + props.imeKonferencije, {
          credentials: 'include',
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        }).then((response) => {
  
          if (response.status >= 300 && response.status < 600) {
            response.json().then((data) => { 
              alert(data.message); 
            });
          } else {
            response.json().then((data) => {
              setNaziv(data[0].naziv);
              setPin(data[0].pin);
              setPbr(data[0].pbr);
              setAdresa(data[0].adresa);
              setMjesto(data[0].mjesto);
              setVideoURL(data[0].urlVideo);
              if (data[0].vrijemePocetka) {
                setStartTime(data[0].vrijemePocetka.slice(0, -3));
              } else {
                setStartTime('');
              }
              
              if (data[0].vrijemeKraja) {
                setEndTime(data[0].vrijemeKraja.slice(0, -3));
              } else {
                setEndTime('');
              }
  
              // Dohvati sponzore
              fetch('/api/pokrovitelj', {
                credentials: 'include',
                method: 'GET',
                headers: {
                  'Content-Type': 'application/json',
                },
              }).then((response) => {
  
                if (response.status >= 300 && response.status < 600) {
                  response.json().then((data) => { 
                    alert(data.message); 
                  });
                } else {
                  response.json().then((sponsorsData) =>{
                    setSponsors(sponsorsData);
  
                    fetch('/api/konferencija/prikaziAdminuSponzoreKonf/' + props.imeKonferencije, {
                      credentials: 'include',
                      method: 'GET',
                      headers: {
                        'Content-Type': 'application/json',
                      },
                    }).then((response) => {
                      if (response.status >= 300 && response.status < 600) {
                        response.json().then((data) => { 
                          alert(data.message); 
                        });
                      }else {
                      response.json().then((selectedSponsorsData) => {
                        setSelectedSponsors(selectedSponsorsData);
                      })};
                    });
                  });
                }
              });
            });
          }
        });
      } catch (error) {
        console.error('Error:', error);
      }
    };
  
    fetchData();
  }, [props.imeKonferencije]);
  

  

  async function handleSubmit(e) {
    e.preventDefault();
    if(!props.canSubmit){
      alert('Već ste završili konferenciju');
      return;}
    var finalBodyArray = [];
    selectedSponsors.forEach((izbor) => {
      finalBodyArray.push(`sponzori=${izbor}`);
    });
    // Postavite svoju logiku za slanje podataka na server
    startTime = startTime.replace('T', ' ');
    endTime= endTime.replace('T', ' ');
    const body = `urlVideo=${videoURL}&vrijemePocetka=${startTime}&vrijemeKraja=${endTime}&mjesto=${mjesto}&pbr=${pbr}&adresa=${adresa}`;
    if(selectedSponsors.length == 0)finalBodyArray.push(`sponzori=`);
    var finalBody = body + '&' + finalBodyArray.join('&');
    
      fetch('/api/konferencija/nadopuniKonf/' + props.imeKonferencije, {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: finalBody 
  }).then((response) => { //front je uspio poslat zahtjev backendu
      var stariDiv = document.getElementsByClassName('alert-container')[0]; //u slucaju da je osoba vec fulala izbrisi stari div
      if (stariDiv && stariDiv.parentElement) {
          stariDiv.parentElement.removeChild(stariDiv);
      }
      if (response.status >= 300 && response.status < 600) {
          response.json().then((data) => { 
              var noviDiv = document.createElement('div'); //ubaci div za tekst greske
              noviDiv.className = 'alert-container';
              noviDiv.textContent = data.message; //iz data izvuci poruku
              var udiv = document.getElementsByClassName('change-data-form')[0]; //ovo prilagoditi ovom htmlu dole za svaku stranicu ce bit drukcije
              udiv.insertBefore(noviDiv, udiv.firstElementChild);
          });}else alert ('Podaci uspješno promijenjeni')}
  )
  .catch((error) => {
      console.error('Error:', error);
  });

  }

  return (
    <div className="centered-wrapper">
      <div className="container">
        <h2>Unos podataka o konferenciji</h2>
        <form className='change-data-form' onSubmit={handleSubmit}>
        <div>
  <label>Naziv: {props.imeKonferencije}</label>
</div>
          <div>
            <label>Pin:</label>
            {pin}
          </div>
          <div>
            <label>Video URL:</label>
            <input
              type="text"
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
            <div className='sponsors'>Odaberite sponzore:</div>
            <select
             multiple
              value={selectedSponsors}
              onChange={(e) => setSelectedSponsors(Array.from(e.target.selectedOptions, (option) => option.value))}
            >
              {["Niti jedan", ...sponsors].map((sponsor) => (
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
