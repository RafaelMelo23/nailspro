package com.rafael.nailspro.webapp.model.repository;

import com.rafael.nailspro.webapp.model.entity.user.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {

    @Modifying
    @Query("UPDATE Professional p set p.isActive = false WHERE p.id = :id")
    void deactivateProfessional(@Param("id") Long id);
}
