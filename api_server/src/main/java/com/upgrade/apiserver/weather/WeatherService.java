package com.upgrade.apiserver.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;


public interface WeatherService {

    HttpResponse getWeatherData();

}
