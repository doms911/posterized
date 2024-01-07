// AddAuthor.jsx
import React, {useEffect, useState} from 'react';
import './login.css';
import Header from './Header';

function AddAuthor(props) {

    const isLoggedIn = props.isLoggedIn;
    const onLogout = props.onLogout;
    const [email, setEmail] = useState('');
    const [name, setName] = useState('');
    const [surname, setSurname] = useState('');
    const [naslov, setNaslov] = useState('');
    const [posterFile, setPosterFile] = useState(null);
    const [presentationFile, setPresentationFile] = useState(null);
    const [conferences, setConferences] = useState([]);
    const [selectedConference, setSelectedConference] = useState('');

    const handlePosterChange = (event) => {
        setPosterFile(event.target.files[0]);
    }

    const handlePresentationChange = (event) => {
        setPresentationFile(event.target.files[0]);
    }
    useEffect(() => {
        // Dohvati popis konferencija
        const fetchConferences = async () => {
            try {
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
            } catch (err) {
                console.error(err.message);
            }
        };        

        fetchConferences();
    }, []);

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
            const response = await fetch('api/radovi/napravi', {
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
                alert('Autor uspješno dodan');
            }

        }catch(error) {
            console.error('Error:', error);
        }
    }

    return (
        <div className="centered-wrapper">
            <Header isLoggedIn={isLoggedIn} onLogout={onLogout}/>
            <div className="container">
                <h2>Dodavanje rada i autora</h2>
                <form id ="moj" onSubmit={handleSubmit}>
                    <div>
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
                        <button type="submit">Dodaj Autora i Rad</button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default AddAuthor;
