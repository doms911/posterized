// Where your buttons are located
import React from 'react';
import { Link } from 'react-router-dom';

const HomePage = () => {
    return (
        <div>
            <h1>Welcome to the Homepage</h1>
            <Link to="/Register">
                <button>Register</button>
            </Link>
            <Link to="/login">
                <button>Login</button>
            </Link>
        </div>
    );
};

export default HomePage;
