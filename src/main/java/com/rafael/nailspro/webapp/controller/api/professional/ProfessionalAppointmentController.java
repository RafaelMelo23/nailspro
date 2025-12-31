package com.rafael.nailspro.webapp.controller.api.professional;

import com.rafael.nailspro.webapp.model.dto.appointment.ProfessionalAppointmentScheduleDTO;
import com.rafael.nailspro.webapp.model.entity.user.UserPrincipal;
import com.rafael.nailspro.webapp.service.appointment.ProfessionalAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/professional/appointments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PROFESSIONAL')")
public class ProfessionalAppointmentController {

    private final ProfessionalAppointmentService service;

    @GetMapping
    public ResponseEntity<List<ProfessionalAppointmentScheduleDTO>> findByDay(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
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
        service.confirmAppointment(appointmentId, clientId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{appointmentId}/finish")
    public ResponseEntity<Void> finishAppointment(
            @PathVariable Long appointmentId,
            @RequestParam Long clientId
    ) {
        service.finishAppointment(appointmentId, clientId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{appointmentId}/cancel")
    public ResponseEntity<Void> cancelAppointment(
            @PathVariable Long appointmentId,
            @RequestParam Long clientId
    ) {
        service.cancelAppointment(appointmentId, clientId);
        return ResponseEntity.noContent().build();
    }
}
