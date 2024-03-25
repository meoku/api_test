package com.upgrade.apiserver;

import com.upgrade.apiserver.weather.WeatherController;
import com.upgrade.apiserver.weather.WeatherEntity;
import com.upgrade.apiserver.weather.WeatherRepository;
import com.upgrade.apiserver.weather.WeatherService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

@SpringBootTest
class ApiServerApplicationTests {

    @Autowired
    private WeatherController weatherController;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private WeatherRepository weatherRepository;

    @DisplayName("Controller End-Point Test")
    @Test
    void testWeatherController() {
        assert weatherController != null;
    }


    @DisplayName("Test GetWeatherDataToAPI Function")
    @Test
    void testGetWeatherDataToAPI() {
        // given
        final String x = "37.549328709";
        final String y = "126.913624675";
        final String address = "test location";
        // when
        Boolean testCase = weatherService.getWeatherDataToAPI(x, y);

        // then
        System.out.println(testCase);
        assertEquals(testCase.booleanValue(), true);

    }

    @DisplayName("Test AddWeatehrDataInDB Function")
    @Test
    void testAddWeatherDataInDB() {

        //given

        // Current Temp Data
        JSONObject jsonObject = new JSONObject();
        JSONObject current = new JSONObject();
        JSONObject weatherData = new JSONObject();
        JSONArray weather = new JSONArray();

        weatherData.put("icon", "01d");
        weatherData.put("description", "맑음");
        weatherData.put("main", "Clear");
        weatherData.put("id", 800);
        weather.add(0, weatherData);

        jsonObject.put("temp", 8.03);
        jsonObject.put("visibility", 10000);
        jsonObject.put("uvi", 2.42);
        jsonObject.put("dt", 1709627849);
        jsonObject.put("weather", weather);
        current.put("current", jsonObject);

        JSONObject dailyData = new JSONObject();
        JSONArray dailyArray = new JSONArray();
        JSONObject daily = new JSONObject();

        JSONObject dailyTemp = new JSONObject();
        dailyTemp.put("min", 5.82);
        dailyTemp.put("max", 12.02);
        dailyData.put("dt", 1710212400);
        dailyData.put("temp", dailyTemp);

        dailyArray.add(0, dailyData);
        dailyArray.add(1, dailyData);
        daily.put("daily", dailyArray);
        current.put("daily", dailyArray);

        //when

        //then


    }

    @DisplayName("Test WeatherRepository findClosestByDatetime")
    @Test
    void findClosestByDatetime() {

        //given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 현재 시간을 LocalDateTime 객체로 가져오기
        LocalDateTime datetime = LocalDateTime.now();

        // LocalDateTime 객체를 문자열로 변환
        String stringDatetime = datetime.format(formatter);
        System.out.println("Formatted DateTime: " + stringDatetime);

        // 문자열을 LocalDateTime 객체로 역 변환
        LocalDateTime parsedDateTime = LocalDateTime.parse(stringDatetime, formatter);
        System.out.println("Parsed DateTime: " + parsedDateTime);

        //when
        List<WeatherEntity> weatherEntityList = weatherRepository.findClosestByDatetime(parsedDateTime);


        //then
        Assertions.assertNotNull(weatherEntityList);
        System.out.println("시간 형태 데이터가 들어 갔을 때 : " + weatherEntityList);
    }


    @DisplayName("Test Scheduler")
    @Test
    @Scheduled(cron = "0 0 0/1 * * *", zone = "Asia/Seoul")
    void schedulerTest() {

    }
}
