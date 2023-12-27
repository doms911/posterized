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


const App = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

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
                    Cookies.remove('user');
                    setIsLoggedIn(false);
                }
            })
            .catch(error => {
                console.error('Logout error:', error);
            });
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
                {isLoggedIn && <Route path="/addConference" element={<AddConference />}/>}
                {isLoggedIn && <Route path="/conferenceInput" element={<ConferenceInput />}/>}
                {!isLoggedIn && <Route path="/forgot-password" element={<ForgotPassword />}/>}
                {!isLoggedIn && <Route path="/live" element={<VideoStream />}/>}
                {!isLoggedIn && <Route path="/changePassword" element={<ChangePassword />}/>}
                {isLoggedIn && <Route path="/superadmin" element={<SuperAdmin />}/>}
                
            </Routes>
        </Router>
    );
};


export default App;
