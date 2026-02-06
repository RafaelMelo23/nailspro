package com.rafael.nailspro.webapp.application.notification;

import com.rafael.nailspro.webapp.domain.model.Appointment;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.notification.AppointmentMessageInfoDTO;
import com.rafael.nailspro.webapp.infrastructure.helper.DateAndZoneHelper;
import com.rafael.nailspro.webapp.infrastructure.helper.TenantUrlProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class AppointmentMessageBuilder {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private final TenantUrlProvider urlProvider;
    private final DateAndZoneHelper dateHelper;

    public String buildAppointmentConfirmationMessage(Appointment appointment) {
        String allServiceNames = Stream.concat(
                Stream.of(appointment.getMainSalonService().getName()),
                appointment.getAddOns().stream().map(addon -> addon.getService().getName())
        ).collect(Collectors.joining(", "));

        return formatConfirmationTemplate(appointment, allServiceNames);
    }

    private String formatConfirmationTemplate(Appointment appointment, String allServices) {
        String firstName = extractFirstName(appointment.getClient().getFullName());

        ZonedDateTime appointmentTime =
                dateHelper.toZonedDateTime(appointment.getStartDate(), appointment.getTenantId());

        String mainServiceValue = appointment.getMainSalonService().getValue().toString();
        BigDecimal addOnsServicesValue = appointment.getAddOns().stream()
                .map(addon -> addon.getService().getValue())
                .map(BigDecimal::new)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String totalValue = new BigDecimal(mainServiceValue).add(addOnsServicesValue).toString();

        return """
                Olá, %s! ✨
                
                Sua reserva na %s está confirmada!
                
                📅 Data: %s às %s
                👤 Profissional: %s
                💅 Serviços: %s
                
                💰 Resumo de Valores:
                - Principal: %s
                - Extras: %s
                - Total: %s
                
                Estamos ansiosos para te receber! Caso precise cancelar/reagendar, acesse: %s
                """.formatted(
                firstName,
                appointment.getSalonTradeName(),
                appointmentTime.format(DATE_FORMATTER),
                appointmentTime.format(TIME_FORMATTER),
                appointment.getProfessional().getFullName(),
                allServices,
                mainServiceValue,
                addOnsServicesValue.toString(),
                totalValue,
                urlProvider.buildCancelAppointmentUrl(appointment.getTenantId(), appointment.getId())
        );
    }

    private static String extractFirstName(String fullName) {
        if (fullName == null || fullName.isEmpty()) return "cliente";

        return fullName.split("\\s+")[0];
    }

    public String buildAppointmentReminderMessage(Appointment appointment) {
        String firstName = extractFirstName(appointment.getClient().getFullName());

        return """
            Olá, %s! 🕒
            
            Passando para lembrar do seu momento hoje na %s.
            
            ⏰ Horário: %s
            👤 Profissional: %s
            
            Preparamos tudo para te receber! ✨
            
            Se houver algum imprevisto, por favor, avise-nos ou acesse o link para cancelar/reagendar: %s
            """.formatted(
                firstName,
                appointment.getSalonTradeName(),
                dateHelper.toZonedDateTime(appointment.getStartDate()).format(TIME_FORMATTER),
                appointment.getProfessional().getFullName(),
                urlProvider.buildCancelAppointmentUrl(appointment.getTenantId(), appointment.getId())
        );
    }
}
