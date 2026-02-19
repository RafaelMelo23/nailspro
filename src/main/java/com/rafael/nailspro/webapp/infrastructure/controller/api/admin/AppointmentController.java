package com.rafael.nailspro.webapp.infrastructure.controller.api.admin;

import com.rafael.nailspro.webapp.application.admin.salon.profile.AppointmentAuditService;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.AdminUserAppointmentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/v1/admin/appointments")
public class AppointmentController {

    private final AppointmentAuditService service;

    @GetMapping("/users/{userId}")
    public ResponseEntity<Page<AdminUserAppointmentDTO>> listUserAppointments(
            @PathVariable Long userId,
            @PageableDefault(
                    sort = "startDateAndTime"
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(
                service.listUsersAppointments(userId, pageable)
        );
    }
}
