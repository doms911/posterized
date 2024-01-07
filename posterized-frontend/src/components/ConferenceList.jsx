import React, { useState } from 'react';
import Conference from './Conference.jsx';
import Card from './Card.jsx';
import Header from './Header.jsx';
import './ConferenceList.css';

function ConferenceList(props) {
  const [conferences, setConferences] = useState([]);
  const [error, setError] = useState(null);
  const isLoggedIn = props.isLoggedIn;
  const onLogout = props.onLogout;

  React.useEffect(() => {
    fetch('/api/konferencija/prikaziSve')
      .then((data) => data.json())
      .then((conferences) => setConferences(conferences))
      .catch((err) => {
        setError(err.response ? err.response.data.message : 'Nepoznata greška');
      });
  }, []);

  const izbrisiKonferenciju = async (naziv) => {
    try {
      await fetch(`/api/konferencija/izbrisiKonf/${naziv}`, { method: 'GET' }).then((response) => {
        if (response.status >= 300 && response.status < 600) {
          response.json().then((data) => { 
            alert(data.message); 
          });
        } else {
          alert('Konferencija uspješno izbrisana');
          setConferences((prevConferences) => prevConferences.filter((conf) => conf.naziv !== naziv));}})
    } catch (err) {
      setError(err.response ? err.response.data.message : 'Nepoznata greška');
    }
  };

  return (
    <div className='pagee'>
      <Header isLoggedIn={isLoggedIn} onLogout={onLogout} />
      <div className="naslov">Popis svih konferencija</div>
      {error && (
        <div className="alert-container">
          <p>{error}</p>
        </div>
      )}
      <Card isLoggedIn={isLoggedIn} onLogout={onLogout}>
        {conferences.map((conference) => (
          <div className="conference" key={conference.naziv}>
            <Conference conference={conference} />
            <button id='delete' onClick={() => izbrisiKonferenciju(conference.naziv)}>Obriši</button>
          </div>
        ))}
      </Card>
    </div>
  );
}

export default ConferenceList;
