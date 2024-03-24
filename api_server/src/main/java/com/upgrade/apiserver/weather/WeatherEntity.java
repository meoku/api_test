package com.upgrade.apiserver.weather;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@Entity
@Table(name = "weather_info")
public class WeatherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "datetime")
    private LocalDateTime datetime;

    @Column(name = "temp")
    private String temp;

    @Column(name = "uvi")
    private String uvi;

    @Column(name = "weather")
    private String weather;

    @Column(name = "weather_id")
    private String weatherId;

    @Column(name = "min_temp")
    private String minTemp;

    @Column(name = "max_temp")
    private String maxTemp;
}
