import React, { useEffect, useState } from 'react';
import "./Pictures.css"; // Kreirajte i stilizujte po potrebi
import Header from "./Header";
import Cookies from 'js-cookie';
import axios from 'axios';
import Sponsors from "./Sponsors";
import WeatherForecast from "./WeatherForecast";

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

    const handleDownload = async (imageUrl) => {
        console.log(imageUrl);
        try {
          const response = await axios.get('/api/fotografija/preuzmi', {
            params: {
                url: imageUrl,
            },
            responseType: 'arraybuffer',
        });
    
            const blob = new Blob([response.data], { type: response.headers['content-type'] });
            const downloadUrl = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = downloadUrl;
            link.setAttribute('download', 'image.jpg');
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        } catch (error) {
            console.error('Error downloading image:', error);
        }
    };

    return (
        <div className="page-container">
            <Header isLoggedIn={isLoggedIn} onLogout={onLogout}/>
            <Sponsors />
            <WeatherForecast/>
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