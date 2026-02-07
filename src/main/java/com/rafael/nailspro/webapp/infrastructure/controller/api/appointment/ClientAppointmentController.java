package com.rafael.nailspro.webapp.infrastructure.controller.api.appointment;

import com.rafael.nailspro.webapp.infrastructure.dto.appointment.AppointmentCreateDTO;
import com.rafael.nailspro.webapp.domain.model.UserPrincipal;
import com.rafael.nailspro.webapp.application.client.ClientAppointmentUseCase;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.ProfessionalAvailabilityDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointment/client")
public class ClientAppointmentController {

    private final ClientAppointmentUseCase service;

    @PostMapping
    public ResponseEntity<Void> scheduleAppointment(@RequestBody AppointmentCreateDTO appointmentDTO,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {

        service.createAppointment(appointmentDTO, userPrincipal);
        return ResponseEntity.status(201).build();
    }

    @PatchMapping("/{appointmentId}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long appointmentId,
                                                  @AuthenticationPrincipal UserPrincipal userPrincipal) {

        service.cancelAppointment(appointmentId, userPrincipal.getUserId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/schedules/{professionalExternalId}")
    public ResponseEntity<ProfessionalAvailabilityDTO> findAvailableProfessionalTimes(
            @PathVariable String professionalExternalId,
            @RequestParam int serviceDurationInSeconds) {

        return ResponseEntity.ok(service.findAvailableTimes(professionalExternalId, serviceDurationInSeconds));
    }
}
