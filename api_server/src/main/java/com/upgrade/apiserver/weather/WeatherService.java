package com.upgrade.apiserver.weather;



public interface WeatherService {

    /*
     * @param lat : 예보 지점 X 좌표
     * @param lon : 예보 지점 y 좌표
     * */
    Boolean getWeatherDataToAPI(String lat, String lon);

    String getWeatherDataForPage(String currentTime);
}
