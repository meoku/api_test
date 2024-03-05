package com.upgrade.apiserver.weather;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
public class WeatherController {

    private WeatherService weatherService;

    private WeatherEntity weather;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public String testPageConnect() {
        return "main";
    }

    //37.549328709, 126.913624675 : 합정역 좌표
    // TODO :: DB에 집어넣기
    @GetMapping("/api/weather")
    public String getWeatherDataToAPI() {
        weatherService.getWeatherDataToAPI("37.549328709", "126.913624675");
        return "OK";
    }

    // TODO :: DB에서 가져오기
}
