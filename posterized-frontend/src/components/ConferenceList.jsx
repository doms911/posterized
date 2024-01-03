// ConferenceList.jsx
import * as React from "react";
import Conference from './Conference.jsx';
import Card from "./Card.jsx";
import Header from "./Header.jsx";
import "./ConferenceList.css";


function ConferenceList(props) {
  const [conferences, setConferences] = React.useState([]);
  const isLoggedIn = props.isLoggedIn;
  const onLogout = props.onLogout;


  React.useEffect(() => {
    fetch('/api/konferencija/prikaziSve')
      .then(data => data.json())
      .then(conferences => setConferences(conferences))
  }, []);

  const izbrisiKonferenciju = async (naziv) => {
    try {
      await fetch(`/api/konferencija/izbrisiKonf/${naziv}`, { method: 'GET' });
      setConferences(prevConferences => prevConferences.filter(conf => conf.naziv !== naziv));
    } catch (error) {
      console.error('Greška prilikom brisanja konferencije:', error);
    }
  };

  console.log(isLoggedIn)

  return (
    <div>
    <Header isLoggedIn={isLoggedIn} onLogout={onLogout} />
    <Card title="Konferencije" isLoggedIn={isLoggedIn} onLogout={onLogout}>
      {conferences.map(conference => (
        <div key={conference.naziv}>
          <Conference conference={conference} />
          <button onClick={() => izbrisiKonferenciju(conference.naziv)}>Obriši</button>
        </div>
      ))}
    </Card>
    </div>
  );
}

export default ConferenceList;
