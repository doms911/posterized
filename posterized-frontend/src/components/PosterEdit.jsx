import React, { useState, useEffect } from 'react';
import './login.css';

function PosterEdit(props) {
    const [email, setEmail] = useState('');
    const [name, setName] = useState('');
    const [surname, setSurname] = useState('');
    const [naslov, setNaslov] = useState('');
    const [posterFile, setPosterFile] = useState(null);
    const [presentationFile, setPresentationFile] = useState(null);
    const [conferences, setConferences] = useState([]);
    const [selectedConference, setSelectedConference] = useState('');
  
    
  useEffect(() => {
    // Dohvati prethodne podatke
    const fetchData = async () => {
      try {
        fetch('/api/radovi/' + props.naslov, {
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
              setName(data[0].ime);
              setSurname(data[0].prezime);
              setEmail(data[0].email);
              setNaslov(data[0].naslov);
              setPosterFile(data[0].poster);
              setPresentationFile(data[0].pptx);
              setSelectedConference(data[0].nazivKonf);    
            });
          }
        });

        fetch('/api/konferencija/prikaziAdminuNazive', {
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
                    setConferences(data);
                });
            }
        });
      } catch (error) {
        console.error('Error:', error);
      }
    }
  
    fetchData();
  }, [props.naslov]);

const handlePosterChange = (event) => {
    setPosterFile(event.target.files[0]);
}

const handlePresentationChange = (event) => {
    setPresentationFile(event.target.files[0]);
}
  

const handleSubmit = async (e) =>  {
    e.preventDefault();
    const formData = new FormData();
    formData.append('ime', name);
    formData.append('prezime', surname);
    formData.append('email', email);
    formData.append('naslov', naslov);
    formData.append('poster', posterFile);
    formData.append('pptx', presentationFile);
    formData.append('nazivKonf', selectedConference);
    try {
        const response = await fetch('api/radovi/nadopuniRad/' + props.naslov, {
            method: 'POST',
            body: formData,
        });

        // Uklanjanje prethodnih poruka o greškama, ako postoje
        var stariDiv = document.getElementsByClassName('alert-container')[0];
        if (stariDiv && stariDiv.parentElement) {
            stariDiv.parentElement.removeChild(stariDiv);
        }

        // Ako je odgovor sa servera greška
        if (!response.ok) {
            const data = await response.json();
            var noviDiv = document.createElement('div');
            noviDiv.className = 'alert-container';
            noviDiv.textContent = data.message;
            var udiv = document.getElementsByClassName('container')[0];
            udiv.insertBefore(noviDiv, document.getElementById("moj"));
        } else {
            alert('Podaci uspješno poslani!');
        }
        props.toggle();

    }catch(error) {
        console.error('Error:', error);
    }
}

  return (
    <div className="centered-wrapper">
            <div className="container">
                <h2>Uređivanje rada</h2>
                <form id ="moj" onSubmit={handleSubmit}>
                    <div className='namee'>
                        <label>Ime:</label>
                        <input
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                        />
                    </div>
                    <div>
                        <label>Prezime:</label>
                        <input
                            type="text"
                            value={surname}
                            onChange={(e) => setSurname(e.target.value)}
                            required
                        />
                    </div>
                    <div>
                        <label>Email:</label>
                        <input
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                    <div>
                        <label>Naslov Rada:</label>
                        <input
                            type="text"
                            value={naslov}
                            onChange={(e) => setNaslov(e.target.value)}
                            required
                        />
                    </div>
                    <div>
                        <label>Poster (PDF, max 1000KB):</label>
                        <input
                            type="file"
                            accept=".pdf"
                            onChange={handlePosterChange}
                            required
                        />
                    </div>
                    <div>
                        <label>Prezentacija (PPTX/PPT, max 1000KB):</label>
                        <input
                            type="file"
                            accept=".ppt,.pptx"
                            onChange={handlePresentationChange}
                        />
                    </div>
                    <div>
                        <label>Konferencija:</label>
                        <select
                            value={selectedConference}
                            onChange={(e) => setSelectedConference(e.target.value)}
                            required
                        >
                            <option value="">Odaberite konferenciju</option>
                            {conferences.map((conf, index) => (
                                <option key={index} value={conf}>{conf}</option>
                            ))}
                        </select>
                    </div>
                    <div>
                        <button type="submit">Dodaj autora i rad</button>
                    </div>
                </form>
            </div>
        </div>
  );
}

export default PosterEdit;
