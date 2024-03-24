package com.upgrade.apiserver.weather;

import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

public interface WeatherService {

    /*
     * @param lat : 예보 지점 X 좌표
     * @param lon : 예보 지점 y 좌표
     * */
    Boolean getWeatherDataToAPI(String lat, String lon);

    void getWeatherDataForPage();
}
