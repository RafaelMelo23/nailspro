package com.rafael.nailspro.webapp.domain.repository;

import com.rafael.nailspro.webapp.domain.enums.appointment.RetentionStatus;
import com.rafael.nailspro.webapp.domain.model.RetentionForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface RetentionForecastRepository extends JpaRepository<RetentionForecast, Long> {

    @Query("""
            SELECT rf FROM RetentionForecast rf
            JOIN SalonProfile sp ON rf.tenantId = sp.tenantId
            WHERE sp.tenantStatus = 'ACTIVE'
            AND rf.predictedReturnDate >= :start
            AND rf.predictedReturnDate <= :end
            AND rf.status IN :statuses
            """)
    List<RetentionForecast> findAllPredictedForecastsBetween(@Param("start") Instant start,
                                                             @Param("end") Instant end,
                                                             @Param("statuses") List<RetentionStatus> statuses);

    @Query("""
            SELECT rf FROM RetentionForecast rf
                       WHERE rf.predictedReturnDate < :now
                                  AND rf.status != :status
            """)
    List<RetentionForecast> findAllExpiredPredictedForecastsAndNotInStatus(@Param("now") Instant now,
                                                                           @Param("status") RetentionStatus status);


}
