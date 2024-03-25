package com.upgrade.apiserver.weather;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface WeatherRepository extends JpaRepository<WeatherEntity, Long> {
    @Query("SELECT w FROM WeatherEntity w ORDER BY ABS(TIMESTAMPDIFF(SECOND, w.datetime, :datetime)) asc LIMIT 1")
    List<WeatherEntity> findClosestByDatetime(@Param("datetime") LocalDateTime datetime);
}