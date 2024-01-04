// HomePage.jsx
import Header from './components/Header';
import { Link } from 'react-router-dom';
import {useEffect, useState} from "react";


const HomePage = (props) => {
    const isLoggedIn = props.isLoggedIn;
    const onLogout = props.onLogout;
    const userRole = localStorage.getItem('userRole');

    const [conferences, setConferences] = useState([]);
    const selectedConference = props.selectedConference;
    const onSelectConference = props.onSelectConference;






    useEffect(() => {
        fetch('/api/konferencija/prikaziAdminuNazive')
            .then(response => response.json())
            .then(data => {
                if (Array.isArray(data)) {
                    setConferences(data);
                } else {
                    console.error('Invalid data structure for conferences:', data);
                }
            })
            .catch(error => console.error('Error fetching conferences:', error));
    }, []);

    const handleSelectConference = (conference) => {
        onSelectConference(conference);
    };

    return (
        <div className="page-container">
            <Header
                isLoggedIn={isLoggedIn}
                onLogout={onLogout}
                selectedConference={selectedConference}
                userRole={userRole}
            />
            <main className="main-content">
                {isLoggedIn ? (
                    <div className="image" style={{ backgroundImage: 'url("https://firebasestorage.googleapis.com/v0/b/posterized-8e1c4.appspot.com/o/headway-F2KRf_QfCqw-unsplash.jpg?alt=media&token=c9dd7219-ea1b-4f47-91f5-da81ec34ebf8")' }}>
                        <div className="text">
                            <h1>Dobrodošli!</h1>
                            <div>Unesite <Link to="/pinInput"> pin</Link> kako biste pristupili konferenciji.</div>

                            {/* Always show conference selection for admin users */}
                            {userRole === 'admin' && (
                                <div>
                                    <h2>Odaberite konferenciju:</h2>
                                    <ul>
                                        {conferences.map((conference, index) => (
                                            <li key={index}>
                                                <button onClick={() => handleSelectConference(conference)}>
                                                    {conference}
                                                </button>
                                            </li>
                                        ))}
                                    </ul>
                                </div>
                            )}

                            {/* Show selected conference if any */}
                            {selectedConference && (
                                <p>Odabrana konferencija: {selectedConference}</p>
                            )}
                        </div>
                    </div>
                ) : (
                    <div className="image"
                         style={{backgroundImage: 'url("https://firebasestorage.googleapis.com/v0/b/posterized-8e1c4.appspot.com/o/headway-F2KRf_QfCqw-unsplash.jpg?alt=media&token=c9dd7219-ea1b-4f47-91f5-da81ec34ebf8")'}}>
                        <div className="text">
                            <h1>Dobrodošli!</h1>
                            <div>
                                <Link to="/login"> Prijavite se</Link> kako biste pristupili konferencijama i ostalim pojedinostima.
                            </div>
                            <div>
                                Nemate račun? <Link to="/register">Registrirajte se</Link>.
                            </div>
                        </div>
                    </div>
                )}
            </main>
        </div>
    );
};

export default HomePage;
