package com.upgrade.apiserver.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class WeatherController {

    private WeatherService weatherService;

    private WeatherEntity weather;

    @Autowired
    public WeatherController(WeatherService weatherService, WeatherEntity weather) {
        this.weatherService = weatherService;
        this.weather = weather;
    }

    @GetMapping("/weather")
    public String testPageConnect() {
        return "main";
    }

    //37.549328709, 126.913624675 : 합정역 좌표
    // TODO :: DB에 집어넣기
    @GetMapping("/api/weather")
    public void getWeatherDataToAPI() {
        String response = weatherService.getWeatherDataToAPI("37.549328709", "126.913624675");
    }

    // TODO :: DB에서 가져오기
}
