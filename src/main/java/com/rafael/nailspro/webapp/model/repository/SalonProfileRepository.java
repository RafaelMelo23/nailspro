package com.rafael.nailspro.webapp.model.repository;

import com.rafael.nailspro.webapp.model.entity.SalonProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalonProfileRepository extends JpaRepository<SalonProfile, Long> {

    Optional<SalonProfile> findByOwner_Id(Long id);


}
