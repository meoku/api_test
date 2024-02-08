import React, {useState, useEffect} from 'react';

function App() {
    const [latitude, setLatitude] = useState(null);
    const [longitude, setLongitude] = useState(null);
    const [error, setError] = useState(null);


    useEffect(() => {
        // 사용자의 위치를 가져오는 함수
        const getLocation = () => {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition((position) => {
                    setLatitude(position.coords.latitude);
                    setLongitude(position.coords.longitude);
                    setError(null);
                }, (error) => {
                    setError('Error retrieving location');
                });
            } else {
                setError('Geolocation is not supported');
            }
        };

        getLocation();
    }, []);

    return (
        <div>
            <h1>Geolocation Example</h1>
            {latitude && longitude ? (<div>
                <p>Latitude: {latitude}</p>
                <p>Longitude: {longitude}</p>
            </div>) : error ? (<p>{error}</p>) : (<p>Loading...</p>)}
        </div>
    );
}

export default App;