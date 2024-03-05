package com.upgrade.apiserver.weather;

import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

public interface WeatherService {

    /*
     * @param lat : 예보 지점 X 좌표
     * @param lon : 예보 지점 y 좌표
     * */
    // TODO :: 배치로 구현하기
    void getWeatherDataToAPI(String lat, String lon);

    void addWeatherDataInDB(@RequestBody String dto);
}
