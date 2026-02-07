package com.rafael.nailspro.webapp.infrastructure.controller.api.appointment;

import com.rafael.nailspro.webapp.application.appointment.AppointmentMessagingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentTest {

    private final AppointmentMessagingUseCase messagingUseCase;

    @PostMapping("/{id}/send-confirmation")
    public ResponseEntity<Void> triggerConfirmation(@PathVariable Long id) {
        messagingUseCase.sendAppointmentConfirmationMessage(id);
        return ResponseEntity.accepted().build();
    }
}
