package com.rafael.agendanails.webapp.application.professional;

import com.rafael.agendanails.webapp.domain.model.Appointment;
import com.rafael.agendanails.webapp.domain.repository.AppointmentRepository;
import com.rafael.agendanails.webapp.domain.repository.AppointmentSpecification;
import com.rafael.agendanails.webapp.infrastructure.dto.appointment.ProfessionalAppointmentScheduleDTO;
import com.rafael.agendanails.webapp.infrastructure.mapper.ProfessionalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessionalScheduleQueryUseCase {

    private final AppointmentRepository appointmentRepository;

    @Transactional(readOnly = true)
    public List<ProfessionalAppointmentScheduleDTO> findProfessionalAppointmentsByDay(Long professionalId,
                                                                                      ZonedDateTime start,
                                                                                      ZonedDateTime end) {
        Specification<Appointment> spec = AppointmentSpecification.fetchRelationships()
                .and(AppointmentSpecification.withProfessionalId(professionalId))
                .and(AppointmentSpecification.withDateRange(start.toInstant(), end.toInstant()));

        return appointmentRepository.findAll(spec)
                .stream()
                .map(ProfessionalMapper::toScheduleDTO)
                .toList();
    }
}