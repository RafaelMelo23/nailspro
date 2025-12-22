package com.rafael.nailspro.webapp.controller.api.admin;

import com.rafael.nailspro.webapp.model.dto.admin.professional.CreateProfessionalDTO;
import com.rafael.nailspro.webapp.model.entity.user.Professional;
import com.rafael.nailspro.webapp.service.ProfessionalService;
import com.rafael.nailspro.webapp.service.admin.professional.AdminProfessionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/professional")
public class AdminProfessionalController {

    private final AdminProfessionalService adminProfessionalService;
    private final ProfessionalService professionalService;

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

    //todo: perhaps change it to a more suitable DTO
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Set<Professional>> getAllProfessionals() {

        return ResponseEntity.ok(professionalService.findAll());
    }
}

