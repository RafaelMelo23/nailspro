package com.rafael.nailspro.webapp.model.repository;

import com.rafael.nailspro.webapp.model.dto.professional.schedule.WorkScheduleRecordDTO;
import com.rafael.nailspro.webapp.model.entity.professional.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {

    List<WorkSchedule> findByProfessional_Id(Long professionalId);
}
