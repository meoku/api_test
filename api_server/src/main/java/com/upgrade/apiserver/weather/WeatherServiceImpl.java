package com.upgrade.apiserver.weather;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@Service
public class WeatherServiceImpl implements WeatherService {
    @Value("${WEATHER_API_KEY}")
    private String apiKey;

    @Override
    public HttpResponse getWeatherData() {
        try {


        } catch (Exception e) {
            System.out.println(e.toString());
            return (HttpResponse) e;
        }
    }
}
