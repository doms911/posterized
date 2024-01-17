//PinInput.jsx
import React, {useEffect, useState} from 'react';
import Header from './Header';
import axios from 'axios';
import "./PinInput.css"
import Cookies from "js-cookie";
import Sponsors from "./Sponsors";
import WeatherForecast from "./WeatherForecast";

const PinInput = (props) => {
    const isLoggedIn = props.isLoggedIn;
    const onLogout = props.onLogout;
    const onPinValidation = props.onPinValidation;

    const [pin, setPin] = useState('');
    const [pinMessage, setPinMessage] = useState('');
    const [conferenceInfo, setConferenceInfo] = useState(null);

    const [winningEntries, setWinningEntries] = useState([]);

    const valid = Cookies.get('conferencePin');

    useEffect(() => {
        const savedInfo = getConferenceInfoFromCookie();
        if (savedInfo) {
            setConferenceInfo(savedInfo);
        }
    }, []);

    const handlePinSubmit = async () => {
        try {
            console.log("Submitting PIN:", pin);
            const response = await axios.get(`/api/prisutan/${pin}`, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            Cookies.set('isPinValid', 'true', { expires: 1 }); // Postavljanje kolačića na 1 dan
            Cookies.set('conferencePin', pin, { expires: 1 });
            setPinMessage(`Konferencija nađena: ${response.data[0].naziv}`);
            setConferenceInfo(response.data);
            console.log("Response Data:", response.data);
            saveConferenceInfoToCookie(response.data);
        }
        catch (err) {
           alert(err.response.data.message);
            Cookies.remove('isPinValid');
            Cookies.remove('conferencePin');
            onPinValidation(false);
        }
    };

    const isConferenceFinished = () => {
        if (conferenceInfo.length > 1) {
            return true;
        }
        return false;
    };

    const renderWinningEntries = () => {
        // Filtrirajte samo one radove koji imaju plasman
        return conferenceInfo.filter(entry => entry.plasman).map((entry, index) => (
            <div key={index}>
                <h4>{entry.plasman}. Mjesto:    {entry.naslov}</h4>
            </div>
        ));
    };

    const saveConferenceInfoToCookie = (conferenceData) => {
        const dataString = JSON.stringify(conferenceData);
        Cookies.set('conferenceInfo', dataString, { expires: 1 }); // Postavljanje kolačića na 1 dan
    };

    const getConferenceInfoFromCookie = () => {
        const dataString = Cookies.get('conferenceInfo');
        return dataString ? JSON.parse(dataString) : null;
    };

    return (
        <div className="pageContainer" >
            <Header isLoggedIn={isLoggedIn} onLogout={onLogout} />
            <div className="pageContainer">
            <div><WeatherForecast/></div>
            <div className='sponsors-wrapper'><Sponsors /></div>
            <div className="centered-wrapper">
                <div className="container">
                    <h2>Pristupite konferenciji</h2>
                    <div>
                        <label htmlFor="pinInput">Unesite PIN:</label>
                        <input
                            type="text"
                            id="pinInput"
                            value={pin}
                            onChange={(e) => setPin(e.target.value)}
                        />
                        <button name="dodaj" onClick={handlePinSubmit}>Provjeri PIN</button>
                        {pinMessage && <p>{pinMessage}</p>}
                    </div>
                    {/* Provjerite da li conferenceInfo nije null i ima elemenata prije pristupa [0] elementu */}
                    {conferenceInfo && conferenceInfo.length > 0 && (
                        <div>
                            <h3>Konferencija: {conferenceInfo[0].naziv}</h3>
                            <p>Mjesto: {conferenceInfo[0].mjesto}</p>
                            <p>Adresa: {conferenceInfo[0].adresa}</p>
                            <p>pbr: {conferenceInfo[0].pbr}</p>
                            <p>Admin: {conferenceInfo[0].admin}</p>
                            <p>Vrijeme početka: {conferenceInfo[0].vrijemePocetka}</p>
                            <p>Vrijeme kraja: {conferenceInfo[0].vrijemeKraja}</p>
                            {/* Pozovite isConferenceFinished kao funkciju */}
                            {isConferenceFinished() && (
                                <div>
                                    <h3>Pobjednici konferencije {conferenceInfo[0].naziv}</h3>
                                    {renderWinningEntries()}
                                </div>
                            )}
                        </div>
                    )}
                </div>
            </div>
            </div>
        </div>
    );
};

export default PinInput;
