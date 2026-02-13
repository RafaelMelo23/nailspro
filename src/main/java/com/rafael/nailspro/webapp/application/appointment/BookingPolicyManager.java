package com.rafael.nailspro.webapp.application.appointment;

import com.rafael.nailspro.webapp.application.service.SalonProfileService;
import com.rafael.nailspro.webapp.domain.enums.AppointmentStatus;
import com.rafael.nailspro.webapp.domain.model.Appointment;
import com.rafael.nailspro.webapp.domain.model.SalonProfile;
import com.rafael.nailspro.webapp.domain.model.SalonService;
import com.rafael.nailspro.webapp.domain.repository.AppointmentRepository;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.booking.AppointmentTimeWindow;
import com.rafael.nailspro.webapp.infrastructure.exception.BusinessException;
import com.rafael.nailspro.webapp.shared.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class BookingPolicyManager {

    private final AppointmentRepository appointmentRepository;
    private final SalonProfileService salonProfileService;

    public void validate(SalonService service, LocalDateTime requestedTime, Long clientId) {
        SalonProfile profile = salonProfileService.getSalonProfileByTenantId(TenantContext.getTenant());

        checkHorizon(profile, clientId, requestedTime);

        if (service.getMaintenanceIntervalDays() != null) {
            validateMainteneceWindow(clientId, service, requestedTime);
        }
    }

    private void checkHorizon(SalonProfile profile,
                              Long clientId,
                              LocalDateTime requestedTime) {

        int allowedDays;

        if (profile.getIsLoyalClientelePrioritized()) {
            allowedDays = isLoyalClient(clientId) ?
                    profile.getLoyalClientBookingWindowDays() :
                    profile.getStandardBookingWindow();
        } else {
            allowedDays = profile.getStandardBookingWindow();
        }

        long daysAhead = ChronoUnit.DAYS.between(LocalDate.now(), requestedTime.toLocalDate());

        if (daysAhead > allowedDays) {
            throw new BusinessException("Sua janela de agendamento não é preferencial, aguarde alguns dias para reservar esta data.");
        }
    }

    public void validateMainteneceWindow(Long clientId,
                                         SalonService service,
                                         LocalDateTime requestedDate) {
        if (service.getMaintenanceIntervalDays() == null) return;

        Appointment lastAppointment =
                appointmentRepository.findFirstByClientIdOrderByStartDateDesc(clientId)
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

    public AppointmentTimeWindow calculateAllowedWindows(List<SalonService> services, Long clientId) {
        SalonProfile profile = salonProfileService.getSalonProfileByTenantId(TenantContext.getTenant());

        LocalDate startDate = determineStartDate(services, clientId);

        int windowDays;
        if (Boolean.TRUE.equals(profile.getIsLoyalClientelePrioritized())) {
            windowDays = isLoyalClient(clientId) ?
                    profile.getLoyalClientBookingWindowDays() :
                    profile.getStandardBookingWindow();
        } else {
            windowDays = profile.getStandardBookingWindow();
        }

        LocalDate endDate = startDate.plusDays(windowDays);

        return AppointmentTimeWindow.builder()
                .start(startDate)
                .end(endDate).build();
    }

    private LocalDate determineStartDate(List<SalonService> services, Long clientId) {
        return services.stream()
                .filter(s -> s.getMaintenanceIntervalDays() != null)
                .findFirst()
                .map(s -> appointmentRepository.findFirstByClientIdOrderByStartDateDesc(clientId)
                        .map(last -> LocalDate.ofInstant(last.getStartDate(), last.getSalonZoneId()).plusDays(s.getMaintenanceIntervalDays() - 3))
                        .orElse(LocalDate.now()))
                .orElse(LocalDate.now());
    }

    private boolean isLoyalClient(Long clientId) {
        if (clientId == null) return false;

        return appointmentRepository.countByClientIdAndAppointmentStatus(clientId, AppointmentStatus.COMPLETED) >= 3;
    }
}