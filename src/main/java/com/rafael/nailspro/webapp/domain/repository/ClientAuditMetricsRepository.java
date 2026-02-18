package com.rafael.nailspro.webapp.domain.repository;

import com.rafael.nailspro.webapp.domain.model.ClientAuditMetrics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientAuditMetricsRepository extends JpaRepository<ClientAuditMetrics, Long> {

    Optional<ClientAuditMetrics> findByClient_Id(Long clientId);
}
