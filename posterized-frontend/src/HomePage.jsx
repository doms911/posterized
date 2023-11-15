// Where your buttons are located
import React from 'react';
import { Link } from 'react-router-dom';

const HomePage = () => {

    function logout() {
        fetch("http://localhost:8080/logout").then(() => {
          logout();
        });
    }
    
    return (
        <div>
            <h1>Welcome to the Homepage</h1>
            <Link to="/Register">
                <button>Register</button>
            </Link>
            <Link to="/login">
                <button>Login</button>
            </Link>
            <button onClick={logout}>Logout</button>
        </div>
    );
};

export default HomePage;
