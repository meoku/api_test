package com.upgrade.apiserver.weather;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

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
    //37.4866168, 127.0471096 : 대한민국 서울특별시 강남구 남부순환로 2748
    @Scheduled(cron = "${SCHEDULES}", zone = "Asia/Seoul") // 매시간 실행
    public void getWeatherDataToAPI() {
        weatherService.getWeatherDataToAPI("37.4866168", "127.0471096");
    }

    // 수동 수집 링크
    @GetMapping("/api/weather")
    public ResponseEntity<String> getManualWeatherDataToAPI() {
        Boolean result;
        result = weatherService.getWeatherDataToAPI("37.4866168", "127.0471096");

        if (Boolean.TRUE.equals(result)) {
            return ResponseEntity.ok("OK");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
        }
    }

    // 파라미터 값으로 현재 시간을 입력 받아야한다.
    @PostMapping("/api/weather-data")
    public String handleWeatherData(@RequestBody JSONObject jsonObject) {
        String currentTime = (String) jsonObject.get("currentTime");
        String result = weatherService.getWeatherDataForPage(currentTime);
        if ("0".equals(result)) {
            return "데이터가 없습니다.";
        } else if ("".equals(result)) {
            return "데이터를 가져오는 중 오류가 발생하였습니다";
        } else {
            return result;
        }
    }

}
