package com.upgrade.apiserver.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import static java.net.http.HttpClient.*;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {


    private final WeatherRepository weatherRepository;

    @Value("${WEATHER_API_KEY}")
    private String appId;
    String lang = "kr";
    String units = "metric";
    WeatherEntity entity = new WeatherEntity();


    @Autowired
    public WeatherServiceImpl(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }


    @Override
    public Boolean getWeatherDataToAPI(String lat, String lon) {

        final String url = MessageFormat.format("https://api.openweathermap.org/data/3.0/onecall?lat={0}&lon={1}&appid={2}&lang={3}&units={4}", lat, lon, appId, lang, units);
        HttpClient client = newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response != null && response.statusCode() == 200) {
                addWeatherDataInDB(response.body());
                return true;
            } else {
                log.error("API 서버와 통신이 되지 않습니다 현재 ResponseCode = {}", response != null ? response.statusCode() : "Unknown");
                return false;
            }
        } catch (Exception e) {
            log.error("DB에 저장하는 동안 버그가 발생하였습니다. = {}", e.toString());
            return false;
        }

    }

    public void addWeatherDataInDB(@RequestBody String jsonString) {
        JSONParser jsonParser = new JSONParser();
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        LocalTime noon = LocalTime.NOON;
        LocalDateTime dateTime = LocalDateTime.of(today, noon);
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        Object millis = dateTime.atZone(zoneId).toInstant().toEpochMilli() / 1000;
        // 동작시간을 저장하기 위한 형 변환
        LocalDateTime workTime = LocalDateTime.of(today, currentTime);


        try {
            JSONObject jsonData = (JSONObject) jsonParser.parse(jsonString);
            JSONObject current = (JSONObject) jsonData.get("current");
            JSONObject weatherData = (JSONObject) jsonParser.parse(current.toJSONString());
            JSONArray weatherArray = (JSONArray) weatherData.get("weather");
            JSONObject weather = (JSONObject) weatherArray.get(0);

            Object description = weather.get("description");
            Object temp = current.get("temp");
            Object uvi = current.get("uvi");
            Object weatherId = weather.get("id");

            JSONObject dailyData = (JSONObject) jsonParser.parse(jsonData.toJSONString());
            JSONArray dailyArray = (JSONArray) dailyData.get("daily");
            JSONObject daily;
            JSONObject dailyTemp;

            Object dt;
            Object maxTemp = 0.0;
            Object minTemp = 0.0;

            for (int i = 0; i < dailyArray.size(); i++) {
                daily = (JSONObject) dailyArray.get(i);
                dt = daily.get("dt");
                if (dt.equals(millis)) {
                    dailyTemp = (JSONObject) daily.get("temp");
                    minTemp = dailyTemp.get("min");
                    maxTemp = dailyTemp.get("max");
                }
            }

            entity.setWeather((String) description);
            entity.setTemp(temp.toString());
            entity.setUvi(uvi.toString());
            entity.setMinTemp(minTemp.toString());
            entity.setMaxTemp(maxTemp.toString());
            entity.setDatetime(workTime);
            entity.setWeatherId(weatherId.toString());

            weatherRepository.save(entity);

        } catch (ParseException e) {
            log.error("error log = {}", e.toString());

        }

    }


    // TODO :: 지역 (좌표 -> 도로명 주소) 기능 추가하기
    @Override
    public String getWeatherDataForPage(String currentTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime current = LocalDateTime.parse(currentTime, formatter);

            ObjectMapper objectMapper = new ObjectMapper();
            Collection<WeatherEntity> weatherData = weatherRepository.findClosestByDatetime(current);
            if (!weatherData.isEmpty()) {
                return objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(weatherData);
            } else {
                return "0";
            }
        } catch (Exception e) {
            log.error("데이터를 가져오는 중에 문제가 발생하였습니다. = {}", e.toString());
            return "";
        }
    }
}
