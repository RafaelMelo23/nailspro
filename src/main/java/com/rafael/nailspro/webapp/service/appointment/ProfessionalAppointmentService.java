package com.rafael.nailspro.webapp.service.appointment;

import com.rafael.nailspro.webapp.model.dto.appointment.ProfessionalAppointmentScheduleDTO;
import com.rafael.nailspro.webapp.model.enums.AppointmentStatus;
import com.rafael.nailspro.webapp.model.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessionalAppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentService appointmentService;

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
                        .startDate(ap.getStartDate())
                        .endDate(ap.getEndDate())

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
}
