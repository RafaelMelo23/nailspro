package com.rafael.nailspro.webapp.infrastructure.controller.api.professional;

import com.rafael.nailspro.webapp.application.professional.ProfessionalAppointmentUseCase;
import com.rafael.nailspro.webapp.domain.model.UserPrincipal;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.ProfessionalAppointmentScheduleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/professional/appointments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PROFESSIONAL')")
public class ProfessionalAppointmentController {

    private final ProfessionalAppointmentUseCase service;

    @GetMapping
    public ResponseEntity<List<ProfessionalAppointmentScheduleDTO>> findByDay(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime end
    ) {
        return ResponseEntity.ok(
                service.findProfessionalAppointmentsByDay(
                        principal.getUserId(),
                        start,
                        end
                )
        );
    }

    @PatchMapping("/{appointmentId}/confirm")
    public ResponseEntity<Void> confirmAppointment(
            @PathVariable Long appointmentId,
            @RequestParam Long clientId
    ) {
        service.markAppointmentAsConfirmed(appointmentId, clientId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{appointmentId}/finish")
    public ResponseEntity<Void> finishAppointment(
            @PathVariable Long appointmentId,
            @RequestParam Long clientId
    ) {
        service.markAppointmentAsFinished(appointmentId, clientId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{appointmentId}/cancel")
    public ResponseEntity<Void> cancelAppointment(
            @PathVariable Long appointmentId,
            @RequestParam Long clientId
    ) {
        service.markAppointmentAsCancelled(appointmentId, clientId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{appointmentId}/miss")
    public ResponseEntity<Void> missedAppointment(
            @PathVariable Long appointmentId,
            @RequestParam Long clientId
    ) {
        service.markAppointmentAsMissed(appointmentId, clientId);
        return ResponseEntity.noContent().build();
    }
}