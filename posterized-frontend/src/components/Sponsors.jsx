import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './Sponsors.css';
import Cookies from "js-cookie"; // Stvorite CSS datoteku za stiliziranje

function Sponsors() {
    const [sponsors, setSponsors] = useState([]);
    const pin = Cookies.get('conferencePin');

    useEffect(() => {
        if (pin) {
            axios.get(`/api/konferencija/pokrovitelji/${pin}`)
                .then(response => {
                    console.log('API response:', response);
                    if (Array.isArray(response.data)) {
                        console.log('Setting sponsors:', response.data);
                        setSponsors(response.data);
                    } else {
                        console.error('Response is not an array:', response.data);
                    }
                })
                .catch(error => {
                    console.error('Error fetching sponsors:', error);
                });
        }
    }, [pin]);


    return (
        <div className="sponsors-container">
            {sponsors.map((sponsor, index) => (
                <a key={index} href={sponsor.url} target="_blank" rel="noopener noreferrer">
                    <img src={sponsor.urlSlike} alt={sponsor.altmedija}/>
                </a>
            ))}
        </div>
    );
}

export default Sponsors;
