import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Cookies from "js-cookie";
import "./WeatherForecast.css";

function WeatherForecast() {
    const [forecastUrl, setForecastUrl] = useState('');
    const [weatherData, setWeatherData] = useState(null);
    const pin = Cookies.get('conferencePin');

    // Dohvat URL-a za prognozu vremena
    useEffect(() => {
        if (pin) {
            axios.get(`/api/konferencija/dohvatiMjesto/${pin}`)
                .then(response => {
                    setForecastUrl(response.data);
                    console.log(forecastUrl);// pretpostavka da je response.data string URL
                })
                .catch(error => {
                    console.error('Error fetching the forecast URL:', error);
                });
        }
    }, [pin]);

    // Dohvat podataka o vremenu kada se dobije forecastUrl
    useEffect(() => {
        if (forecastUrl) {
            axios.get(forecastUrl)
                .then(response => {
                    setWeatherData(response.data);
                    console.log(weatherData);// pretpostavka da je response.data JSON s podacima o vremenu
                })
                .catch(error => {
                    console.error('Error fetching the weather data:', error);
                });
        }
    }, [forecastUrl]);

    return (
        <div className="weather-container">
            {weatherData && weatherData.days && weatherData.days.length > 0 && (
                <div>
                    <h4>Vrijeme u {weatherData.resolvedAddress}</h4>
                    <p>Temperatura: {weatherData.days[0].temp}°C</p>
                    <p>Opis: {weatherData.days[0].description}</p>
                </div>
            )}
        </div>
    );
}

export default WeatherForecast;
