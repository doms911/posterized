// Header.jsx
import React from 'react';
import { Link } from 'react-router-dom';
import "./Header.css";
import Sidebar from './Sidebar'

const Header = (props) => {
    const isLoggedIn = props.isLoggedIn;
    const onLogout = props.onLogout;
    const name = localStorage.getItem('name');
    const userRole = localStorage.getItem('userRole');
 

    return (
        <header className="Header">
            {isLoggedIn ? (
                <div className="Header">
                     <Sidebar userRole = {userRole}/>
                    <div className="LoggedIn">Pozdrav, {name}! Prijavljeni ste u sustav.</div>
                    <div className="LoggedIn"><button onClick={onLogout}>Odjava</button></div>
                </div>
            ) : (
                <div>
                    <Link to="/register">
                        <button>Registracija</button>
                    </Link>
                    <Link to="/login">
                        <button>Prijava</button>
                    </Link>
                </div>
            )}
        </header>
    );
};

export default Header;
