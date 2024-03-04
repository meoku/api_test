import axios from "axios";
import {useEffect, useState} from "react";
import './App.css'

function App() {
    const [state, setState] = useState({icon: '', temp: '', uv: '', maxtemp: '', mintemp: '', state: ''});
    useEffect(() => {
        response();
    }, [state]);

    const response = () => {
        axios.get('http://api.weatherapi.com/v1/forecast.json', {
            params: {
                key: process.env.REACT_APP_WEATHER_API,
                q: '37.550552, 126.9160743',
                days: '',
                lang: 'ko'
            }
        }).then((res) => {
            console.log(res)

            setState({
                icon: `https://${res.data.current.condition.icon}`,
                temp: res.data.current.temp_c,
                uv: res.data.current.uv,
                maxtemp: res.data.forecast.forecastday[0].day.maxtemp_c,
                mintemp: res.data.forecast.forecastday[0].day.mintemp_c,
                state: res.data.current.condition.text,
            })
        })

    }


    //37.550552, 126.9160743 : 회사 좌표
    return (
        <div className={'weather-info'}>
            <div className={'icon-section'}>
                <img src={state.icon} alt={''}/>
            </div>

            <div className={'temp-section'}>
                <h1>{state.temp}</h1>
                <p>{state.maxtemp}/{state.mintemp}</p>
            </div>
            <div className={'infomation-section'}>
                <div className={'state-section'}>
                    <h3>{state.state}</h3>
                </div>
                <div className={'additional-info-section'}>
                    <p>미세먼지 : 나쁨</p>
                    <p>자외선 {state.uv}</p>
                </div>

                <div className={'location-section'}>
                    <p>내위치:</p>
                    <p>서울시 도곡동</p>
                </div>
            </div>
        </div>
    );
}


export default App;
