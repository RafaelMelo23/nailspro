package com.rafael.nailspro.webapp.controller.api;

import com.rafael.nailspro.webapp.model.dto.appointment.AppointmentCreateDTO;
import com.rafael.nailspro.webapp.model.entity.user.UserPrincipal;
import com.rafael.nailspro.webapp.service.salon.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointment")
public class AppointmentController {

    private final AppointmentService service;

    @PostMapping
    public ResponseEntity<Void> scheduleAppointment(@RequestBody AppointmentCreateDTO appointmentDTO,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {

        service.createAppointment(appointmentDTO, userPrincipal);
        return ResponseEntity.status(201).build();
    }

    @PatchMapping("/{appointmentId}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long appointmentId,
                                                  @AuthenticationPrincipal UserPrincipal userPrincipal) {

        service.cancelAppointmentAndFlagClient(appointmentId, userPrincipal.getUserId());
        return ResponseEntity.noContent().build();
    }
}
