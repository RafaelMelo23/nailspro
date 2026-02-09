package com.rafael.nailspro.webapp.domain.repository;

import com.rafael.nailspro.webapp.domain.model.RetentionForecast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetentionForecastRepository extends JpaRepository<RetentionForecast, Long> {
}
