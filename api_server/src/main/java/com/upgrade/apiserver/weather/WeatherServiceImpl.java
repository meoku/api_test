package com.upgrade.apiserver.weather;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpClient.*;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Value("${WEATHER_API_KEY}")
    private String appId;
    String lang = "kr";
    String exclude = "daily";
    String units = "metric";

//    https://api.openweathermap.org/data/3.0/onecall?lang=kr&exclude=hourly&units=metric

    // MEMO :: 검색기능을 넣는게 맞나?
    @Override
    public String getWeatherDataToAPI(String lat, String lon) {
        final String url = "https://api.openweathermap.org/data/3.0/onecall?lat=" + lat + "&lon=" + lon + "&appid=" + appId + "&lang=" + lang + "&exclude=" + exclude + "&units=" + units;

        try {
            HttpClient client = newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
                    //MEMO :: HTTP METHOD 정하는 방법
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();

        } catch (InterruptedException | IOException e) {
            return e.toString();
        } finally {
            //
        }
    }
}
