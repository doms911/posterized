// App.jsx
// eslint-disable-next-line no-unused-vars
import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Register from './components/Register.jsx';
import Login from './components/login.jsx';
import HomePage from './HomePage.jsx';
import AddConference from './components/AddConference.jsx';
import ConferenceInput from './components/ConferenceInput.jsx';
import Cookies from 'js-cookie';
import ForgotPassword from './components/ForgotPassword.jsx';
import VideoStream from './components/videoStream.jsx';
import ChangePassword from './components/changePassword.jsx';
import SuperAdmin from './components/superadmin.jsx';
import AddAdmin from './components/AddAdmin.jsx';
import ConferenceList from './components/ConferenceList.jsx';
import PinInput from './components/PinInput.jsx';
import AddAuthor from "./components/AddAuthor";
import AddSponsor from "./components/AddSponsor.jsx"
import AdminConference from './components/AdminConference.jsx';
import Sidebar from "./components/Sidebar";
import Posters from "./components/Posters";
import Pictures from "./components/Pictures";


const App = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [adminConferences, setAdminConferences] = useState([]);
    const [error, setError] = useState(null);
    const userRole = localStorage.getItem('userRole');

    const [isPinValid, setIsPinValid] = useState(false);

    useEffect(() => {
      const fetchData = async () => {
        if(userRole==="admin") {
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
          setError(err.response ? err.response.data.message : 'Nepoznata greška');
        }}
      };

      fetchData();
    }, []);

    
    useEffect(() => {
      const userCookie = Cookies.get('user');
      if (userCookie) {
        setIsLoggedIn(true);
      }
    }, []);
  
    const handleLogin = () => {
      setIsLoggedIn(true);
    };
  
    const handleLogout = () => {
      fetch("/api/logout", {
        method: 'POST',
        credentials: 'include',
      })
        .then(response => {
          if (response.ok) {
            localStorage.removeItem('username');
            localStorage.removeItem('name');
            localStorage.removeItem('userRole');
            Cookies.remove('user');
            setIsLoggedIn(false);
            window.location.replace('/');
            Cookies.remove('isPinValid');
            var cookies = document.cookie.split(";");
            for (var i = 0; i < cookies.length; i++) {
            var cookie = cookies[i].trim();
            if (cookie.startsWith('votedFor_'))Cookies.remove(cookie.split('=')[0]);}
            Cookies.remove('conferencePin');
            Cookies.remove('conferenceInfo');
          }
        })
        .catch(error => {
          console.error('Logout error:', error);
        });
    };

    const handlePinValidation = (isValid) => {
        setIsPinValid(isValid);
    };


    return (
      <Router>
        <Routes>
          <Route
            path="/"
            element={<HomePage isLoggedIn={isLoggedIn} onLogout={handleLogout} />}
          />

          {!isLoggedIn && <Route path="/register" element={<Register />} />}
          {!isLoggedIn && <Route path="/login" element={<Login onLogin={handleLogin} />} />}
          {isLoggedIn && <Route path="/addConference" element={<AddConference isLoggedIn={isLoggedIn} onLogout={handleLogout} />} />}
          {isLoggedIn && <Route path="/conferenceInput" element={<ConferenceInput isLoggedIn={isLoggedIn} onLogout={handleLogout} />} />}
          {isLoggedIn && <Route path="/pinInput" element={<PinInput isLoggedIn={isLoggedIn} onLogout={handleLogout} onPinValidation={handlePinValidation}/>} />}
          {!isLoggedIn && <Route path="/forgot-password" element={<ForgotPassword />} />}
          {!isLoggedIn && <Route path="/changePassword" element={<ChangePassword />} />}
          {isLoggedIn && <Route path="/superadmin" element={<SuperAdmin />} />}
          {isLoggedIn && <Route path="/addAdmin" element={<AddAdmin isLoggedIn={isLoggedIn} onLogout={handleLogout} />} />}
          {isLoggedIn && <Route path="/conferenceList" element={<ConferenceList isLoggedIn={isLoggedIn} onLogout={handleLogout} />} />}
          {isLoggedIn && <Route path="/addAuthor" element={<AddAuthor isLoggedIn={isLoggedIn} onLogout={handleLogout} />} />}
          {isLoggedIn && <Route path="/addSponsor" element={<AddSponsor isLoggedIn={isLoggedIn} onLogout={handleLogout} />} />}
          {isLoggedIn && <Route path="/conferenceInput" element={<AddSponsor isLoggedIn={isLoggedIn} onLogout={handleLogout} />} />}
            {isLoggedIn && <Route path="/videoStream" element={<VideoStream isLoggedIn={isLoggedIn} onLogout={handleLogout} />} />}
            {isLoggedIn && <Route path="/pictures" element={<Pictures isLoggedIn={isLoggedIn} onLogout={handleLogout} />} />}
            {isLoggedIn && <Route path="/posters" element={<Posters isLoggedIn={isLoggedIn} onLogout={handleLogout} />} />}
  
          {adminConferences.length > 0 && (
            adminConferences.map((adminConference, index) => (
              <Route
                key={index}
                path={`/konferencija/${adminConference}`}
                element={<AdminConference isLoggedIn={isLoggedIn} onLogout={handleLogout} adminConference={adminConference} />}
              />
            ))
          )}
        </Routes>
      </Router>
    );
  };
  
  export default App;
  