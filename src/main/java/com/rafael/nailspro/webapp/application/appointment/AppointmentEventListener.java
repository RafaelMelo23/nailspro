package com.rafael.nailspro.webapp.application.appointment;

import com.rafael.nailspro.webapp.infrastructure.dto.appointment.booking.AppointmentBookedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AppointmentEventListener {

    private final AppointmentMessagingUseCase messagingUseCase;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBookedAppointment(AppointmentBookedEvent appointmentBookedEvent) {

        messagingUseCase.sendAppointmentConfirmationMessage(
                appointmentBookedEvent.appointmentId()
        );
    }
}
