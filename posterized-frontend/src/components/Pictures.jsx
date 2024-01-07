import React, { useEffect, useState } from 'react';
import "./Pictures.css"; // Kreirajte i stilizujte po potrebi
import Header from "./Header";
import Cookies from 'js-cookie';
import axios from 'axios';

function Pictures({ isLoggedIn, onLogout }) {
    const [imageUrls, setImageUrls] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        const fetchImages = async () => {
            const pin = Cookies.get('conferencePin');
            console.log(pin);
            if (pin) {
                try {
                    const response = await axios.get(`/api/fotografija/dohvatiSlike/${pin}`);
                    setImageUrls(response.data); // Pretpostavimo da API vraća listu URL-ova
                } catch (err) {
                    setErrorMessage(err.response.data.message || 'Greška prilikom dohvatanja slika.');
                }
            } else {
                setErrorMessage('PIN nije definisan.');
            }
        };

        fetchImages();
    }, []);

    const handleDownload = (url) => {
        axios({
            url: url,
            method: 'GET',
            responseType: 'blob',
        }).then((response) => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'image.jpg'); 
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        });
    };

    return (
        <div className="page-container">
            <Header isLoggedIn={isLoggedIn} onLogout={onLogout}/>
            <div className="centered-wrapper">
                {errorMessage ? (
                    <div className="error-message">
                        <p>{errorMessage}</p>
                    </div>
                ) : (
                    <div className="images">
                        {imageUrls.map((url, index) => (
                            <div key={index} className="image-item">
                                <img src={url} alt={`image-${index}`} />
                                <button onClick={() => handleDownload(url)}>Preuzmi</button>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}

export default Pictures;
