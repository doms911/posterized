import React from 'react';
import "./videoStream.css"

function videoStream() {
    // Ovdje možete postaviti URL vašeg video izvora
    const videoSrc = 'your_video_source_url_here';

    return (
        <div className="video-stream-container">
            <h2>Video Prijenos</h2>
            <video width="720" height="480" controls>
                <source src={videoSrc} type="video/mp4" />
                Nažalost, vaš preglednik ne podržava ugrađeni video.
            </video>
        </div>
    );
}

export default videoStream;