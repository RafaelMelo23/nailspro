package com.rafael.nailspro.webapp.application.appointment;

import com.rafael.nailspro.webapp.application.service.SalonProfileService;
import com.rafael.nailspro.webapp.domain.model.Appointment;
import com.rafael.nailspro.webapp.domain.model.SalonService;
import com.rafael.nailspro.webapp.domain.repository.AppointmentRepository;
import com.rafael.nailspro.webapp.infrastructure.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Log4j2
@Service
@RequiredArgsConstructor
public class BookingPolicyManager {

    private final AppointmentRepository appointmentRepository;
    private final SalonProfileService salonProfileService;

    public void validate(SalonService service, LocalDateTime requestedTime, Long clientId) {

        checkHorizon(service, requestedTime);

        if (service.getMaintenanceIntervalDays() != null) {
            validateMainteneceWindow(clientId, service, requestedTime);
        }
    }

    private void checkHorizon(SalonService service, LocalDateTime requestedTime) {
        if (service.getBookingHorizonDays() == null) return;

        long daysAhead = ChronoUnit.DAYS.between(LocalDate.now(), requestedTime.toLocalDate());

        if (daysAhead > service.getBookingHorizonDays()) {
            throw new BusinessException("Agenda ainda não aberta para este período");
        }
    }

    public void validateMainteneceWindow(Long clientId,
                                         SalonService service,
                                         LocalDateTime requestedDate) {
        if (service.getMaintenanceIntervalDays() == null) return;

        Appointment lastAppointment =
                appointmentRepository.findFirstByClientIdAndTenantIdOrderByStartDateDesc(clientId)
                        .orElseThrow(() -> new BusinessException(
                                "Este serviço de manutenção exige um histórico de aplicação prévio"));

        ZoneId salonZoneId = salonProfileService.getSalonZoneId(lastAppointment.getTenantId());

        long daysSinceLastService = ChronoUnit.DAYS.between(
                lastAppointment.getStartDate().atZone(salonZoneId),
                requestedDate.atZone(salonZoneId)
        );

        int threshold = service.getMaintenanceIntervalDays();
        int minDays = threshold - 3;
        int maxDays = threshold + 5;

        if (daysSinceLastService < minDays) throw new BusinessException("Intervalo muito curto para nova manutenção");
        if (daysSinceLastService > maxDays) {
            throw new BusinessException("Prazo de manutenção excedido (" + daysSinceLastService + " dias). " +
                    "A estrutura da unha está comprometida");
        }


    }
}
