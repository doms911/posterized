// HomePage.jsx
import React from 'react';
import { Link } from 'react-router-dom';

const HomePage = (props) => {
    const isLoggedIn = props.isLoggedIn;
    const onLogout = props.onLogout;
    const username = localStorage.getItem('username'); // Assuming you store the username in localStorage

    return (
        <div>
            <h1>Welcome to the Homepage</h1>
            {isLoggedIn ? (
                <div>
                    <p>Pozdrav, {username}! Ulogirani ste u sustav.</p>
                    <button onClick={onLogout}>Logout</button>
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
        </div>
    );
};

export default HomePage;
