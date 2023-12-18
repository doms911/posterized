// Header.jsx
import React from 'react';
import { Link } from 'react-router-dom';
import "./Header.css";
import Sidebar from './Sidebar'

const Header = (props) => {
    const isLoggedIn = props.isLoggedIn;
    const onLogout = props.onLogout;
    const name = localStorage.getItem('name'); // Assuming you store the username in localStorage

    return (
        <header className="Header">
            {isLoggedIn ? (
                <div className="Header">
                    <Sidebar />
                    <div className="LoggedIn">Pozdrav, {name}! Ulogirani ste u sustav.</div>
                    <div className="LoggedIn"><button onClick={onLogout}>Logout</button></div>
                </div>
            ) : (
                <div>
                    <Link to="/register">
                        <button>Register</button>
                    </Link>
                    <Link to="/login">
                        <button>Login</button>
                    </Link>
                </div>
            )}
        </header>
    );
};

export default Header;
