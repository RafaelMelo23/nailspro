package com.rafael.nailspro.webapp.application.messages;

import com.rafael.nailspro.webapp.domain.model.Appointment;
import com.rafael.nailspro.webapp.domain.model.RetentionForecast;
import com.rafael.nailspro.webapp.infrastructure.helper.DateAndZoneHelper;
import com.rafael.nailspro.webapp.infrastructure.helper.TenantUrlProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
@Component
@RequiredArgsConstructor
public class RetentionMessageBuilder {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM");
    private final TenantUrlProvider urlProvider;
    private final DateAndZoneHelper dateHelper;

    public String buildRetentionMessage(RetentionForecast retentionForecast) {
        Appointment originAppointment = retentionForecast.getOriginAppointment();
        Instant expectedReturn = retentionForecast.getPredictedReturnDate();

        ZonedDateTime appointmentTime =
                dateHelper.toZonedDateTime(expectedReturn, originAppointment.getTenantId());

        return String.format("""
                Oi, %s!
                Passando para lembrar que sua próxima sessão de %s
                está prevista para %s.
                Como nossa agenda costuma lotar rápido,
                liberei o link para você garantir sua vaga com antecedência: %s""",
                originAppointment.getClient().getFullName(),
                originAppointment.getMainSalonService().getName(),
                appointmentTime.format(DATE_FORMATTER),
                urlProvider.buildBookAppointmentUrl(originAppointment.getTenantId()));
    }

}
