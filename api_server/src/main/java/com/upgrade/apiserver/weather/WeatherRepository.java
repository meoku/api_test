package com.upgrade.apiserver.weather;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeatherRepository extends JpaRepository<WeatherEntity, Long> {
}