import React from 'react';
import "./videoStream.css"

function videoStream() {
    //hardcodiran url, mijenjat kad dode backend
    const videoSrc = 'neki url';

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