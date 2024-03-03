package com.upgrade.apiserver.weather;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    private static WeatherService weatherService;

    @GetMapping("/weather")
    public String TestConnect() {
        return "main";
    }

}
