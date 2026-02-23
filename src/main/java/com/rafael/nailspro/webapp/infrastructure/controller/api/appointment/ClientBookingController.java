package com.rafael.nailspro.webapp.infrastructure.controller.api.appointment;

import com.rafael.nailspro.webapp.application.client.ClientAppointmentBookingUseCase;
import com.rafael.nailspro.webapp.domain.model.UserPrincipal;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.AppointmentCreateDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.ProfessionalAvailabilityDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.professional.FindProfessionalAvailabilityDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/booking")
public class ClientBookingController {

    private final ClientAppointmentBookingUseCase service;

    @PostMapping
    public ResponseEntity<Void> bookAppointment(@RequestBody AppointmentCreateDTO appointmentDTO,
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

    @GetMapping("/{professionalExternalId}/availability")
    public ResponseEntity<ProfessionalAvailabilityDTO> findAvailableProfessionalTimes(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable String professionalExternalId,
            @RequestParam int serviceDurationInSeconds,
            @RequestParam List<Long> servicesIds
    ) {

        FindProfessionalAvailabilityDTO dto = FindProfessionalAvailabilityDTO.builder()
                .professionalExternalId(professionalExternalId)
                .serviceDurationInSeconds(serviceDurationInSeconds)
                .servicesIds(servicesIds)
                .build();

        return ResponseEntity.ok(service.findAvailableTimes(dto, userPrincipal.getUserId()));
    }
}