import React, { useState, useEffect } from 'react';
import Header from './Header';
import { Link } from 'react-router-dom';
import ConferenceInput from './ConferenceInput';
import PosterEdit from './PosterEdit'
import './AdminConference.css';
import Modal from './Modal';


const AdminConference = (props) => {
  const [error, setError] = useState(null);
  const isLoggedIn = props.isLoggedIn;
  const onLogout = props.onLogout;
  const { adminConference } = props;
  const [selectedImages, setSelectedImages] = useState([]);
  const [canSubmitForm, setCanSubmitForm] = useState(true);
  const [receivedPapers, setReceivedPapers] = useState([]);
  const [seen, setSeen] = useState(false);
  const [editedPaper, setEditedPaper] = useState(null);
  const [awardLocation, setAwardLocation] = useState('');
  const [awardTime, setAwardTime] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  


  useEffect(() => {
    // Učitaj sve radove koje ste dobili
    const fetchReceivedPapers = async () => {
      try {
        const response = await fetch(`/api/konferencija/prikaziAdminuKonf/${adminConference}`, {
          method: 'GET',
          credentials: 'include',
        });

        if (response.status >= 300 && response.status < 600) {
          const data = await response.json();
          alert(data.message);
        } else {
          const data = await response.json();
          setReceivedPapers(data);
        }
      } catch (error) {
        console.error(error.message || 'Nepoznata greška');
      } 
    };

    fetchReceivedPapers();
  }, [adminConference]);

  function togglePop(naslov) {
    setEditedPaper(naslov);
    setSeen(!seen);
  }
  

  const handleImageChange = (e) => {
    const files = e.target.files;
    setSelectedImages(files);
  };


  const handleImageUpload = async () => {
    const formData = new FormData();
    for (let i = 0; i < selectedImages.length; i++) {
      formData.append('slike', selectedImages[i]);
    }

    fetch(`/api/fotografija/${adminConference}`, {
        method: 'POST',
        credentials: 'include',
        body: formData,
      }).then((response) => {
        if (response.status >= 300 && response.status < 600) {
          response.json().then((data) => { 
            alert(data.message); 
          });
        } else alert('Slike uspješno poslane.');})
  };

  const handleFinishConference = async () => {
    const isConfirmed = window.confirm("Jeste li sigurni da želite završiti konferenciju? Više nećete moći urediti njene podatke, a svim sudionicima bit će poslane obavijesti o dodjeli nagrada");
    if(!isConfirmed) return;
    // Onemogući slanje forme pod (a)
    setCanSubmitForm(false);

    // Pošalji GET na /api/radovi/plasman/<naziv konf koju zavrsava>
    try {
      fetch(`/api/radovi/plasman/${adminConference}`, {
        method: 'GET',
        credentials: 'include',
      }).then((response) => {
        if (response.status >= 300 && response.status < 600) {
          response.json().then((data) => { 
            alert(data.message); 
          });
        } else {
          setIsModalOpen(true);
    


          // Pošalji POST na www/api/konferencija/zavrsiKonf/<naziv konf koju zavrsava>
         
        }
      });
    } catch (error) {
      console.error(error.message || 'Nepoznata greška');
    }
  }

   const endConferenceApi = async () => {
             // Spremi detalje dodjele nagrada

             if(awardTime.toString()==''||awardLocation==''){
              alert('Datum i lokacija dodjele nagrada moraju biti postavljeni .');
             }else{
             const formData = new URLSearchParams();
             formData.append('vrijeme', awardTime.toString().replace( "T" , " " ));
             formData.append('lokacija', awardLocation);

             
    try {
          fetch(`/api/konferencija/zavrsiKonf/${adminConference}`, {
            method: 'POST',
            credentials: 'include',
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: formData.toString(),
          }).then((response) => {
            if (response.status >= 300 && response.status < 600) {
              response.json().then((data) => { 
                alert(data.message); 
              });
            } else {
              setIsModalOpen(false);
              alert('Konferencija uspješno završena.');
            }
          });
    } catch (error) {
      console.error(error.message || 'Nepoznata greška');
    }
  }
  }
    

  const handleDeletePaper = async (naziv) => {
    const isConfirmed = window.confirm("Jeste li sigurni da želite izbrisati rad?");
    if(!isConfirmed) return;
    try {
      fetch(`/api/radovi/izbrisi/${naziv}`, { method: 'GET' }).then((response) => {
        if (response.status >= 300 && response.status < 600) {
          response.json().then((data) => { 
            alert(data.message); 
          });
        } else {
          alert('Rad uspješno izbrisan');
          setReceivedPapers((prevPapers) => prevPapers.filter((paper) => paper.naslov !== naziv));
        }
      });
    } catch (err) {
      setError(err.response ? err.response.data.message : 'Nepoznata greška');
    }
  }


  return (
    <div className="page">
            <Modal isOpen={isModalOpen} >
      <div className ='content'>
          <h4 className='black-font'>Završetak konferencije</h4>
          <div>
            <label className='black-font'>Datum dodjele nagrada:</label>
            <input
              type="datetime-local"
              id="endTime"
              value={awardTime || ''}
              onChange={(e) => setAwardTime(e.target.value)}
            />
            </div>
            <div>
             <label className='black-font'>Lokacija dodjele nagrada:</label>
            <input
              type="text"
              id="awardLocation"
              value={awardLocation || ''}
              onChange={(e) => setAwardLocation(e.target.value)}
            />
          </div>
          <button onClick={() => setIsModalOpen(false)}>
         Close
        </button>
        <button onClick={endConferenceApi}>
         Završi konferenciju
        </button>
        </div>
      </Modal>

      <div>
        <Header isLoggedIn={isLoggedIn} onLogout={onLogout} />
        <h1 className='name'>{adminConference}</h1>
        <div>
        <div className='things'>
        <div>
        {<ConferenceInput imeKonferencije={adminConference} canSubmit={canSubmitForm}/>}
        </div>
        <div className='content'>
        <input type="file" accept=".jpg, .jpeg, .png" multiple onChange={handleImageChange} />
        <button onClick={handleImageUpload}>Pošalji slike</button>
        </div>
      </div>
      <div className ='content'>
        <button onClick={handleFinishConference}>Završi konferenciju</button>
      </div>
      </div>
  <h2 className='content'>Radovi:</h2>
  {receivedPapers.length > 1 ? (
    <ul>
      {receivedPapers
        .filter((paper, index) => index !== 0) // Izuzimanje rada na indeksu 0
        .map((paper) => (
          <li className="conference" key={paper.naslov}>
            <div>Naslov: {paper.naslov}</div>
            <div>Ime: {paper.ime}</div>
            <div>Prezime: {paper.prezime}</div>
            <div>Mail: {paper.mail}</div>
            <div>Ukupno glasova: {paper.ukupnoGlasova}</div>
            <div >Pptx:</div> 
            <div><a className='url' href={paper.urlPptx} target="_blank">{paper.urlPptx}</a></div>
            <div>Poster:</div> 
            <div> <a className='url' href={paper.urlPoster} target="_blank">{paper.urlPoster}</a></div>
            <button onClick={() => togglePop(paper.naslov)}>Uredi</button>
            {seen && editedPaper === paper.naslov && (
            <PosterEdit naslov={editedPaper} toggle={() => togglePop(null)} />
          )}
            <button key={`delete-${paper.naslov}`} onClick={() => handleDeletePaper(paper.naslov)}>
              Izbriši
            </button>
          </li>
        ))}
    </ul>
  ) : (
    <p className='content'>Nema dostavljenih radova.</p>
  )}
</div>

    </div>
  );
};

export default AdminConference;
