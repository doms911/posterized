import React, { useEffect, useState } from 'react';
import Header from "./Header";
import axios from "axios";
import Cookies from "js-cookie";
import "./Posters.css";
import Sponsors from "./Sponsors";
import WeatherForecast from "./WeatherForecast";

function Posters({ isLoggedIn, onLogout }) {
    const [works, setWorks] = useState([]);
    const pin = Cookies.get('conferencePin');

    useEffect(() => {
        if (pin) {
            axios.get(`/api/konferencija/dohvatiRadove/${pin}`)
                .then(response => {
                    setWorks(response.data); // Assuming response.data is an array of works
                })
                .catch(error => {
                    console.error('Error fetching the works: ', error);
                });
        }
    }, [pin]);

    const handleVote = async (title, index) => {
        const encodedTitle = encodeURIComponent(title);
        const url = `/api/prisutan/glasaj/${encodedTitle}`;

        try {
            fetch(url, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
            }).then((response) => {
                if (response.status >= 300 && response.status < 600) {
                    response.json().then((data) => {alert(data.message);})}
                else{
            console.log('Success: Vote cast for', title);
            alert('Vaš glas je zabilježen!');

            // Set a cookie to indicate that the user has voted for this work
            Cookies.set(`votedFor_${encodedTitle}`, 'true', { expires: 1 });}}) // Expires in 1 day
        } catch (error) {
            console.error('Error during voting:', error);
        }
    };

    const hasVoted = (title) => {
        const encodedTitle = encodeURIComponent(title);
        return Cookies.get(`votedFor_${encodedTitle}`) === 'true';
    };

    return (
        <div className="posters-container">
            <Header isLoggedIn={isLoggedIn} onLogout={onLogout} />
            <div><WeatherForecast/></div>
            {works.map((work, index) => (
                <div key={index} className="work-container">
                    <div className="work-info">
                        <h2>{work.naslov}</h2>
                        <p>{work.ime} {work.prezime}</p> {/* Assuming 'ime' and 'prezime' are the properties */}
                    </div>
                    <div className="content-container">
                        <div className="pdf-container">
                            {work.urlPoster ? (
                                <div className="pdf-and-link-container">
                                    <iframe
                                        src={work.urlPoster}
                                        className="pdf-document"
                                        frameBorder="0"
                                        title={work.naslov}
                                    ></iframe>
                                    <a href={work.urlPptx} target="_blank" rel="noopener noreferrer"
                                       className="presentation-link">Link za prezentaciju!</a>
                                </div>
                            ) : (
                                <p>Loading PDF...</p>
                            )}
                        </div>
                        <div className="voting-container">
                            {work.plasman && work.ukupnoGlasova ? (
                                <div>
                                    <p>Broj Glasova: {work.ukupnoGlasova}</p>
                                    <p>Plasman: {work.plasman}</p>
                                </div>
                            ) : hasVoted(work.naslov) ? (
                                <p>Hvala na glasanju!</p>
                            ) : (
                                <button type="button" onClick={() => handleVote(work.naslov, index)}>Glasaj</button>
                            )}
                        </div>
                    </div>
                </div>

            ))}
            <div className='sponsor'><Sponsors /></div>
        </div>
    );
}

export default Posters;
