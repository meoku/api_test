import {useState} from "react";

const {kakao} = window;

function App() {

    const [address, setAddress] = useState(null);
    const getAddress = () => {
        const geocoder = new kakao.maps.services.Geocoder(); // 좌표 -> 주소로 변환해주는 객체
        const coord = new kakao.maps.LatLng(37.550552, 126.9160743)
        const callback = function (result, status) {
            if (status === kakao.maps.services.Status.OK) {
                setAddress(result[0].address);
            }
        };
        geocoder.coord2Address(coord.getLng(), coord.getLat(), callback);
    };

    return (

        <div className="App">

            <button onClick={getAddress}>현재 좌표의 주소 얻기</button>
            {address && (
                <div id='map'>
                    현재 좌표의 주소는..
                    <p>address_name: {address.address_name}</p>
                    <p>region_1depth_name: {address.region_1depth_name}</p>
                    <p>region_2depth_name: {address.region_2depth_name}</p>
                    <p>region_3depth_name: {address.region_3depth_name}</p>
                </div>
            )}


        </div>
    )
        ;
}

export default App;
