import React, { useEffect, useState } from 'react';
import "./videoStream.css";
import Header from "./Header";
import Cookies from "js-cookie";
import axios from "axios";
import Sponsors from "./Sponsors";
import WeatherForecast from "./WeatherForecast";

function VideoStream({ isLoggedIn, onLogout }) {
    const [videoUrl, setVideoUrl] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        const fetchVideo = async () => {
            const pin = Cookies.get('conferencePin');
            if (pin) {
                try {
                    const response = await axios.get(`/api/konferencija/dohvatiVideo/${pin}`);
                    const videoId = extractYouTubeId(response.data); // Funkcija za izvlačenje ID-a videa
                    setVideoUrl(`https://www.youtube.com/embed/${videoId}`);
                } catch (err) {
                    setErrorMessage(err.response.data.message || 'Došlo je do greške prilikom dohvata videa.');
                }
            } else {
                setErrorMessage('PIN nije definisan.');
            }
        };

        fetchVideo();
    }, []);

    // Funkcija za izvlačenje YouTube ID-a iz URL-a
    const extractYouTubeId = (url) => {
        const regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
        const match = url.match(regExp);

        return (match && match[2].length === 11) ? match[2] : null;
    };

    return (
        <div><Header isLoggedIn={isLoggedIn} onLogout={onLogout}/>
        <div className="video-stream-container">
            <div className='video'>
                {errorMessage ? (
                    <div className="error-message">
                        <p>{errorMessage}</p>
                    </div>
                ) : (
                    <iframe id='frame'
                        width="720"
                        height="480"
                        src={videoUrl}
                        frameBorder="0"
                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                        allowFullScreen
                        title="Live Stream Video">
                    </iframe>
                )}
            </div>
        </div>
        <div className='sponsorsClassName'><Sponsors /></div>
        </div>
    );
}

export default VideoStream;
