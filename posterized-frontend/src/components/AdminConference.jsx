import React, { useState, useEffect } from 'react';
import Header from './Header';
import { Link } from 'react-router-dom';
import ConferenceInput from './ConferenceInput';

const AdminConference = (props) => {
    const [error, setError] = useState(null);
    const [isActive, setIsActive] = useState(false); 
  const isLoggedIn = props.isLoggedIn;
  const onLogout = props.onLogout;
  const { adminConference } = props;
  const [selectedImages, setSelectedImages] = useState([]);
  const [canSubmitForm, setCanSubmitForm] = useState(true);
  const [awardCeremonyDetails, setAwardCeremonyDetails] = useState({
    time: '',
    location: '',
  });
  const [receivedPapers, setReceivedPapers] = useState([]);

  useEffect(() => {
    // Učitaj sve radove koje ste dobili
    const fetchReceivedPapers = async () => {
      try {
        const response = await fetch(`/api/konferencija/prikaziAdminuKonf/${adminConference}`, {
          method: 'GET',
          credentials: 'include',
        });

        if (!response.ok) {
          throw new Error(`Server error: ${response.status}`);
        }

        const data = await response.json();
        console.log(data);
        setReceivedPapers(data);
      } catch (error) {
        console.error(error.message || 'Nepoznata greška');
      }
    };

    fetchReceivedPapers();
  }, [adminConference]);

  const handleImageChange = (e) => {
    const files = e.target.files;
    setSelectedImages(files);
  };

  const changeIsActive = () => {
    setIsActive(!isActive);
  }

  const handleImageUpload = async () => {
    const formData = new FormData();
    for (let i = 0; i < selectedImages.length; i++) {
      formData.append('slike', selectedImages[i]);
    }

    try {
      const response = await fetch(`/api/fotografija/${adminConference}`, {
        method: 'POST',
        credentials: 'include',
        body: formData,
      });

      if (!response.ok) {
        throw new Error(`Server error: ${response.status}`);
      }

      alert('Slike uspješno poslane.');
    } catch (error) {
      console.error(error.message || 'Nepoznata greška');
    }
  };

  const handleFinishConference = async () => {
    // Onemogući slanje forme pod (a)
    setCanSubmitForm(false);

    // Pošalji GET na /api/radovi/plasman/<naziv konf koju zavrsava>
    try {
      const getResponse = await fetch(`/api/radovi/plasman/${adminConference}`, {
        method: 'GET',
        credentials: 'include',
      });

      if (!getResponse.ok) {
        throw new Error(`Server error: ${getResponse.status}`);
      }

      // Iskoci forma za unos detalja dodjele nagrada
      const time = prompt('Unesite vrijeme dodjele nagrada:');
      const location = prompt('Unesite lokaciju dodjele nagrada:');

      // Spremi detalje dodjele nagrada
      setAwardCeremonyDetails({
        time: time,
        location: location,
      });

      // Pošalji POST na /api/konferencija/zavrsiKonf/<naziv konf koju zavrsava>
      const postResponse = await fetch(`/api/konferencija/zavrsiKonf/${adminConference}`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(awardCeremonyDetails),
      });

      if (!postResponse.ok) {
        throw new Error(`Server error: ${postResponse.status}`);
      }

      alert('Konferencija uspješno završena.');
    } catch (error) {
      console.error(error.message || 'Nepoznata greška');
    }
  };

  const handleDeletePaper = async (naziv) => {
    try {
      await fetch(`/api/radovi/izbrisi/${naziv}`, { method: 'GET' });
      setReceivedPapers((prevPapers) => prevPapers.filter((paper) => paper.naziv !== naziv));
    } catch (err) {
      setError(err.response ? err.response.data.message : 'Nepoznata greška');
    }
  };

  return (
    <div>
      <div>
        <Header isLoggedIn={isLoggedIn} onLogout={onLogout} />
        <h1>{adminConference}</h1>

        <button onClick={changeIsActive}>
          Nadopuni podatke
        </button>
        {isActive && <ConferenceInput imeKonferencije={adminConference}/>}
        <input type="file" accept=".jpg, .jpeg, .png" multiple onChange={handleImageChange} />
        <button onClick={handleImageUpload}>Pošalji slike</button>
      </div>
      <div>
        <button onClick={handleFinishConference}>Završi konferenciju</button>
      </div>
      <div>
  <h2>Radovi:</h2>
  {receivedPapers.length > 1 ? (
    <ul>
      {receivedPapers
        .filter((paper, index) => index !== 0) // Izuzimanje rada na indeksu 0
        .map((paper) => (
          <li key={paper.naslov}>
            <div>Naslov: {paper.naslov}</div>
            <div>Ime: {paper.ime}</div>
            <div>Prezime: {paper.prezime}</div>
            <div>Mail: {paper.mail}</div>
            <div>Ukupno glasova: {paper.ukupnoGlasova}</div>
            <div>Pptx: {paper.urlPptx}</div>
            <div>Poster: {paper.urlPoster}</div>
            <button key={`delete-${paper.naslov}`} onClick={() => handleDeletePaper(paper.naslov)}>
              Izbriši
            </button>
          </li>
        ))}
    </ul>
  ) : (
    <p>Nema dostavljenih radova.</p>
  )}
</div>

    </div>
  );
};

export default AdminConference;
