package com.upgrade.apiserver.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private static WeatherService weatherService;

    @GetMapping("/weather")
    public String testPageConnect() {
        return "main";
    }

    @PostMapping("/api/weather")
    public void getWeatherData() {
//        weatherService.
    }

}
