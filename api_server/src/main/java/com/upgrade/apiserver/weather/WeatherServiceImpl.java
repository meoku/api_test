package com.upgrade.apiserver.weather;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.time.*;

import static java.net.http.HttpClient.*;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {


    @Value("${WEATHER_API_KEY}")
    private String appId;
    String lang = "kr";
    String units = "metric";

    @Override
    public void getWeatherDataToAPI(String lat, String lon) {

        final String url = MessageFormat.format("https://api.openweathermap.org/data/3.0/onecall?lat={0}&lon={1}&appid={2}&lang={3}&units={4}", lat, lon, appId, lang, units);
        HttpClient client = newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response != null) {
                addWeatherDataInDB(response.body());
            }
        } catch (Exception e) {
            log.error("error log = {}", "response is null (1. 파라 미터 값 체크 or 2. API 호출 횟수 체크)");
        }
    }

    // TODO :: 파싱한 데이터 DB에 넣기
    @Override
    public void addWeatherDataInDB(@RequestBody String jsonString) {
        JSONParser jsonParser = new JSONParser();
        LocalDate today = LocalDate.now();
        LocalTime noon = LocalTime.NOON;
        LocalDateTime dateTime = LocalDateTime.of(today, noon);
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        Object millis = dateTime.atZone(zoneId).toInstant().toEpochMilli() / 1000;

        try {
            JSONObject jsonData = (JSONObject) jsonParser.parse(jsonString);
            JSONObject current = (JSONObject) jsonData.get("current");
            JSONObject weatherData = (JSONObject) jsonParser.parse(current.toJSONString());
            JSONArray weatherArray = (JSONArray) weatherData.get("weather");
            JSONObject weather = (JSONObject) weatherArray.get(0);

            Object description = weather.get("description");
            Object temp = current.get("temp");
            Object uvi = current.get("uvi");

            //daily.dt>=date
            //daily.min minTemp
            //daily.max maxTemp

            JSONObject dailyData = (JSONObject) jsonParser.parse(jsonData.toJSONString());
            JSONArray dailyArray = (JSONArray) dailyData.get("daily");
            JSONObject daily;
            JSONObject dailyTemp;

            Object dt;
            Object maxTemp;
            Object minTemp;

            for (int i = 0; i < dailyArray.size(); i++) {
                daily = (JSONObject) dailyArray.get(i);
                dt = daily.get("dt");
                if (dt.equals(millis)) {
                    dailyTemp = (JSONObject) daily.get("temp");
                    minTemp = dailyTemp.get("min");
                    maxTemp = dailyTemp.get("max");
                }
            }


        } catch (ParseException e) {
            log.error("error log = {}", e.toString());

        }

    }
}
