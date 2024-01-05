import React, { useState, useEffect } from 'react';
import AdminConference from './AdminConference.jsx';
import Card from './Card.jsx';
import Header from './Header.jsx';
import './ConferenceList.css';

function ConferenceList(props) {
  const [adminConferences, setAdminConferences] = useState([]);
  const [error, setError] = useState(null);
  const isLoggedIn = props.isLoggedIn;
  const onLogout = props.onLogout;

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch('/api/konferencija/prikaziAdminuNazive', {
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
        setAdminConferences(data);
      } catch (err) {
        setError(`Greška prilikom dohvaćanja konferencija: ${err.message}`);
      }
    };

    fetchData();
  }, []);

  return (
    <div>
      <Header isLoggedIn={isLoggedIn} onLogout={onLogout} />
      <div className="naslov">Popis svih konferencija</div>
      {error && (
        <div className="alert-container">
          <p>{error}</p>
        </div>
      )}
      <Card isLoggedIn={isLoggedIn} onLogout={onLogout}>
        {adminConferences.map((adminConference) => (
          <div className="conference" key={adminConference.naziv}>
            <AdminConference adminConference={adminConference} />
          </div>
        ))}
      </Card>
    </div>
  );
}

export default ConferenceList;
