package com.upgrade.apiserver.weather;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "weather_info")
public class WeatherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long datetime;

    private String temp;
    private Float uvi;
    private String weather;
    private Float minTemp; //일 최저기온
    private Float maxTemp; //일 최대기온
    private Integer visibility; // 가시거리(미세먼지 측정 데이터로 이용?)
}
