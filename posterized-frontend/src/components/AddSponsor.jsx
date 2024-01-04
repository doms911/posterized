import React, { useState } from 'react';
import Header from "./Header";


const AddSponsor = (props) => {
    const isLoggedIn = props.isLoggedIn;
    const onLogout = props.onLogout;
    const userRole = localStorage.getItem('userRole');
    const selectedConference = props.selectedConference;
    const [sponsorData, setSponsorData] = useState({
        url: '',
        naziv: '',
        logo: null,
    });
    const [message, setMessage] = useState('');

    const handleInputChange = (e) => {
        setSponsorData({ ...sponsorData, [e.target.name]: e.target.value });
    };

    const handleFileChange = (e) => {
        setSponsorData({ ...sponsorData, logo: e.target.files[0] });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!sponsorData.logo || !sponsorData.naziv || !sponsorData.url) {
            setMessage('Please fill out all fields and select a logo.');
            return;
        }

        const formData = new FormData();
        formData.append('url', sponsorData.url);
        formData.append('naziv', sponsorData.naziv);
        formData.append('logo', sponsorData.logo);

        fetch('/api/pokrovitelj', {
            method: 'POST',
            body: formData,
        })
            .then((response) => {
                if (!response.ok) {
                    return response.json().then((data) => {
                        throw new Error(data.message || 'Error occurred while adding sponsor');
                    });
                }
                return response.json();
            })
            .then(() => {
                setMessage('Sponsor successfully created!');
                // Reset form or redirect user
            })
            .catch((error) => {
                setMessage(error.message);
            });
    };

    return (
        <div className="centered-wrapper">
            <Header isLoggedIn={isLoggedIn} onLogout={onLogout} selectedConference={selectedConference} userRole={userRole}/>
            <div className="container">
                <h1>Add a Sponsor</h1>
                {message && <div className="alert-container">{message}</div>}
                <form onSubmit={handleSubmit}>
                    <div>
                        <label>Name:</label>
                        <input type="text" name="naziv" value={sponsorData.naziv} onChange={handleInputChange}/>
                    </div>
                    <div>
                        <label>URL:</label>
                        <input type="text" name="url" value={sponsorData.url} onChange={handleInputChange}/>
                    </div>
                    <div>
                        <label>Logo (JPG, JPEG, PNG):</label>
                        <input type="file" name="logo" accept=".jpg, .jpeg, .png" onChange={handleFileChange}/>
                    </div>
                    <button type="submit">Add Sponsor</button>
                </form>
            </div>
        </div>
    );
};

export default AddSponsor;
