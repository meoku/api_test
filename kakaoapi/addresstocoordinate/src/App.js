import DaumPostcode from "react-daum-postcode"

const {kakao} = window;

function App() {
    console.log(`${process.env.REACT_APP_KAKAO_API_KEY}`)

    // MEMO :: 주소를 좌표로
    const onCompletePost = (data) => {
        let geocoder = new kakao.maps.services.Geocoder();
        geocoder.addressSearch(data.address, function (result, status) {
            if (status === kakao.maps.services.Status.OK) {
                const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
                console.log(coords);
            }
            console.log(data.address);
        });
    };
    return (
        <div className="App">
            <DaumPostcode onComplete={onCompletePost}></DaumPostcode>
        </div>
    );
}

export default App;
