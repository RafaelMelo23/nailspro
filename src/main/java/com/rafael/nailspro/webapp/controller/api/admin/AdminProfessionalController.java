package com.rafael.nailspro.webapp.controller.api.admin;

import com.rafael.nailspro.webapp.model.dto.admin.professional.CreateProfessionalDTO;
import com.rafael.nailspro.webapp.service.admin.professional.AdminProfessionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/professional")
public class AdminProfessionalController {

    private final AdminProfessionalService adminProfessionalService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createProfessional(
            @RequestBody CreateProfessionalDTO professionalDTO) {

        adminProfessionalService.createProfessional(professionalDTO);
        return ResponseEntity.status(201).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateProfessional(
            @PathVariable Long id) {

        adminProfessionalService.deactivateProfessional(id);
        return ResponseEntity.noContent().build();
    }
}

