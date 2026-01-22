package com.rafael.nailspro.webapp.application.professional;

import com.rafael.nailspro.webapp.domain.repository.ProfessionalRepository;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.ProfessionalAppointmentScheduleDTO;
import com.rafael.nailspro.webapp.domain.enums.AppointmentStatus;
import com.rafael.nailspro.webapp.domain.repository.AppointmentRepository;
import com.rafael.nailspro.webapp.application.appointment.AppointmentService;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.TimeInterval;
import com.rafael.nailspro.webapp.infrastructure.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfessionalAppointmentUseCase {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentService appointmentService;
    private final ProfessionalRepository repository;

    @Transactional(readOnly = true)
    public List<ProfessionalAppointmentScheduleDTO> findProfessionalAppointmentsByDay(Long professionalId,
                                                                                      LocalDateTime start,
                                                                                      LocalDateTime end) {
        return appointmentRepository
                .findByProfessional_IdAndStartDateBetween(professionalId, start, end)
                .stream()
                .map(ap -> ProfessionalAppointmentScheduleDTO.builder()
                        .appointmentId(ap.getId())

                        .clientId(ap.getClient().getId())
                        .clientName(ap.getClient().getFullName())
                        .clientPhoneNumber(ap.getClient().getPhoneNumber())
                        .clientMissedAppointments(ap.getClient().getMissedAppointments())
                        .clientCanceledAppointments(ap.getClient().getCanceledAppointments())


                        .status(ap.getAppointmentStatus())
                        .totalValue(ap.calculateTotalValue())
                        .observations(ap.getObservations())
                        .startDate(ZonedDateTime.from(ap.getStartDate()))
                        .endDate(ZonedDateTime.from(ap.getEndDate()))

                        .build()
                )
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
