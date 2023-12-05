// HomePage.jsx
import React from 'react';
import Header from './components/Header';

const HomePage = (props) => {
    const isLoggedIn = props.isLoggedIn;
    const onLogout = props.onLogout;

    return (
        <div>
            <h1>Welcome to the Homepage</h1>
            <Header isLoggedIn={isLoggedIn} onLogout={onLogout} />
        </div>
    );
};

export default HomePage;
