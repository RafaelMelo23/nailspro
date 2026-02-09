package com.rafael.nailspro.webapp.application.professional;

import com.rafael.nailspro.webapp.application.appointment.AppointmentService;
import com.rafael.nailspro.webapp.domain.enums.AppointmentStatus;
import com.rafael.nailspro.webapp.domain.repository.AppointmentRepository;
import com.rafael.nailspro.webapp.domain.repository.ProfessionalRepository;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.ProfessionalAppointmentScheduleDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.date.TimeInterval;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.event.AppointmentFinishedEvent;
import com.rafael.nailspro.webapp.infrastructure.exception.BusinessException;
import com.rafael.nailspro.webapp.infrastructure.mapper.ProfessionalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfessionalAppointmentUseCase {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentService appointmentService;
    private final ProfessionalRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<ProfessionalAppointmentScheduleDTO> findProfessionalAppointmentsByDay(Long professionalId,
                                                                                      ZonedDateTime start,
                                                                                      ZonedDateTime end) {

        return appointmentRepository
                .findByProfessional_IdAndStartDateBetween(professionalId, start.toInstant(), end.toInstant())
                .stream()
                .map(ProfessionalMapper::toScheduleDTO)
                .toList();
    }

    @Transactional
    public void confirmAppointment(Long appointmentId, Long clientId) {
        var appointment = appointmentService.findAndValidateAppointmentOwnership(appointmentId, clientId);

        appointment.setAppointmentStatus(AppointmentStatus.CONFIRMED);
    }

    @Transactional
    public void finishAppointment(Long appointmentId, Long clientId) {
        var appointment = appointmentService.findAndValidateAppointmentOwnership(appointmentId, clientId);

        appointment.setAppointmentStatus(AppointmentStatus.FINISHED);

        eventPublisher.publishEvent(new AppointmentFinishedEvent(appointmentId));
    }

    @Transactional
    public void cancelAppointment(Long appointmentId, Long clientId) {
        var appointment = appointmentService.findAndValidateAppointmentOwnership(appointmentId, clientId);

        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
    }

    public void checkIfProfessionalHasTimeConflicts(UUID professionalId, TimeInterval interval) {
        if (repository.hasTimeConflicts(
                professionalId,
                interval.toLocalDateTime(interval.realTimeStart()),
                interval.toLocalDateTime(interval.endTimeWithBuffer()),
                interval.getDayOfWeek())) {
            throw new BusinessException("O profissional já possui um compromisso ou bloqueio neste horário.");
        }
    }
}
