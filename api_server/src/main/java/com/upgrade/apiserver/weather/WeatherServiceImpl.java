package com.upgrade.apiserver.weather;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

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
    WeatherEntity entity = new WeatherEntity();

    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherServiceImpl(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }


    @Override
    public void getWeatherDataToAPI(String lat, String lon) {

        final String url = MessageFormat.format("https://api.openweathermap.org/data/3.0/onecall?lat={0}&lon={1}&appid={2}&lang={3}&units={4}", lat, lon, appId, lang, units);
        HttpClient client = newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response != null && response.statusCode() == 200) {
                addWeatherDataInDB(response.body());
            } else {
                log.error("API 서버와 통신이 되지 않습니다 현재 ResponseCode = {}", response != null ? response.statusCode() : "Unknown");
            }
        } catch (Exception e) {
            log.error("API 서버와 통신 하는 동안 버그가 발생하였습니다.", e.toString());
        }

    }

    @Override
//    public void addWeatherDataInDB(@RequestBody String jsonString) {
//        JSONParser jsonParser = new JSONParser();
//        LocalDate today = LocalDate.now();
//        LocalTime noon = LocalTime.NOON;
//        LocalDateTime dateTime = LocalDateTime.of(today, noon);
//        ZoneId zoneId = ZoneId.of("Asia/Seoul");
//        Object millis = dateTime.atZone(zoneId).toInstant().toEpochMilli() / 1000;
//
//        try {
//            JSONObject jsonData = (JSONObject) jsonParser.parse(jsonString);
//            JSONObject current = (JSONObject) jsonData.get("current");
//            JSONObject weatherData = (JSONObject) jsonParser.parse(current.toJSONString());
//            JSONArray weatherArray = (JSONArray) weatherData.get("weather");
//            JSONObject weather = (JSONObject) weatherArray.get(0);
//
//
//            Object description = weather.get("description");
//            Object temp = current.get("temp");
//            Object uvi = current.get("uvi");
//
//
//            entity.setWeather((String) description);
//            entity.setTemp((String) temp);
//            entity.setUvi((Float) uvi);
//
//            //daily.dt>=date
//            //daily.min minTemp
//            //daily.max maxTemp
//
//            JSONObject dailyData = (JSONObject) jsonParser.parse(jsonData.toJSONString());
//            JSONArray dailyArray = (JSONArray) dailyData.get("daily");
//            JSONObject daily;
//            JSONObject dailyTemp;
//
//
//            Object dt = null;
//            Object maxTemp = 0.0;
//            Object minTemp = 0.0;
//
//            for (int i = 0; i < dailyArray.size(); i++) {
//                daily = (JSONObject) dailyArray.get(i);
//                dt = daily.get("dt");
//                if (dt.equals(millis)) {
//                    dailyTemp = (JSONObject) daily.get("temp");
//                    minTemp = dailyTemp.get("min");
//                    maxTemp = dailyTemp.get("max");
//                }
//            }
//            entity.setMinTemp((Float) minTemp);
//            entity.setMaxTemp((Float) maxTemp);
//            entity.setDatetime((Long) dt);
//
//        } catch (ParseException e) {
//            log.error("error log = {}", e.toString());
//
//        }
//
//    }

    public void addWeatherDataInDB(String jsonString) {
        try {
            JSONObject jsonData = (JSONObject) new JSONParser().parse(jsonString);
            JSONObject current = (JSONObject) jsonData.get("current");
            JSONArray weatherArray = (JSONArray) current.get("weather");
            JSONObject weather = (JSONObject) weatherArray.get(0);

            WeatherEntity entity = new WeatherEntity();
            entity.setWeather((String) weather.get("description"));
            entity.setTemp(String.valueOf(current.get("temp")));
            entity.setUvi(Float.parseFloat(String.valueOf(current.get("uvi"))));

            JSONObject todayWeather = getWeatherForToday(jsonData);
            if (todayWeather != null) {
                entity.setMinTemp(Float.parseFloat(String.valueOf(todayWeather.get("min"))));
                entity.setMaxTemp(Float.parseFloat(String.valueOf(todayWeather.get("max"))));
                entity.setDatetime((Long) todayWeather.get("dt"));
            }

            // Save entity to database
            // weatherRepository.save(entity);
        } catch (ParseException e) {
            log.error("Error parsing weather data JSON", e);
        }
    }

    private JSONObject getWeatherForToday(JSONObject jsonData) {
        JSONArray dailyArray = (JSONArray) jsonData.get("daily");
        for (Object obj : dailyArray) {
            JSONObject daily = (JSONObject) obj;
            long dt = (long) daily.get("dt");
            if (isToday(dt)) {
                return (JSONObject) daily.get("temp");
            }
        }
        return null;
    }

    private boolean isToday(long dt) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(dt), ZoneId.of("Asia/Seoul"));
        return dateTime.toLocalDate().equals(LocalDate.now());
    }

    @Override
    public void getWeatherDataForPage() {
        try {
            entity.getDatetime();
            entity.getWeather();
            entity.getUvi();
            entity.getTemp();
            entity.getMinTemp();
            entity.getMaxTemp();

            weatherRepository.save(entity);

        } catch (Exception e) {
            log.error("error log = {}", e.toString());
        }

    }
}
