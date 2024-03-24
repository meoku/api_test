package com.upgrade.apiserver.weather;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
public class WeatherController {

    private WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public String testPageConnect() {
        return "main";
    }

    //37.549328709, 126.913624675 : 합정역 좌표
    @Scheduled(cron = "${SCHEDULES}", zone = "Asia/Seoul") // 매시간 실행
    @GetMapping("/api/weather")
    public String getWeatherDataToAPI() {
        weatherService.getWeatherDataToAPI("37.549328709", "126.913624675");
        return "OK";
    }

    @GetMapping("/api/weather-data")
    public String getWeatherDataForPage() {
        weatherService.getWeatherDataForPage();
        return "OK";
    }

}
