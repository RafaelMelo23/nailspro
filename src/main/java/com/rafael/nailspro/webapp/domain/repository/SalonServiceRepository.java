package com.rafael.nailspro.webapp.domain.repository;

import com.rafael.nailspro.webapp.domain.model.SalonService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface SalonServiceRepository extends JpaRepository<SalonService, Long> {

    Optional<Set<SalonService>> findByIdIn(Collection<Long> ids);

    @Modifying
    @Query("UPDATE SalonService ss SET ss.active = :active where ss.id = :id")
    void changeSalonServiceVisibility(@Param("id") Long id,
                                      @Param("active") Boolean active);

    @Modifying
    @Query("UPDATE SalonService ss SET ss.isDeleted = TRUE where ss.id = :id")
    void deleteService(@Param("id") Long id);
}
