package com.rafael.nailspro.webapp.infrastructure.controller.api.admin;

import com.rafael.nailspro.webapp.application.admin.professional.ProfessionalManagementService;
import com.rafael.nailspro.webapp.application.professional.ProfessionalQueryService;
import com.rafael.nailspro.webapp.application.professional.ScheduleBlockService;
import com.rafael.nailspro.webapp.application.professional.WorkScheduleService;
import com.rafael.nailspro.webapp.domain.model.Professional;
import com.rafael.nailspro.webapp.infrastructure.dto.admin.professional.CreateProfessionalDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.professional.ProfessionalResponseDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.professional.schedule.WorkScheduleRecordDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.professional.schedule.block.ScheduleBlockOutDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/professional")
public class ProfessionalManagementController {

    private final ProfessionalManagementService managementService;
    private final ProfessionalQueryService queryService;
    private final WorkScheduleService workScheduleService;
    private final ScheduleBlockService scheduleBlockService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createProfessional(
            @Valid @RequestBody CreateProfessionalDTO professionalDTO) {

        managementService.createProfessional(professionalDTO);
        return ResponseEntity.status(201).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateProfessional(
            @PathVariable Long id) {

        managementService.deactivateProfessional(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ProfessionalResponseDTO>> getAllProfessionals() {

        return ResponseEntity.ok(queryService.findAllProfessionals());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/schedule/{professionalId}")
    public ResponseEntity<Set<WorkScheduleRecordDTO>> getProfessionalWorkSchedule(@PathVariable Long professionalId) {

        return ResponseEntity.ok(workScheduleService.getWorkSchedules(professionalId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/schedule/block/{professionalId}")
    public ResponseEntity<List<ScheduleBlockOutDTO>> getProfessionalScheduleBlocks(@PathVariable Long professionalId,
                                                                                   @RequestParam LocalDateTime dateAndTime) {

        return ResponseEntity.ok(scheduleBlockService.getBlocks(professionalId, Optional.of(dateAndTime)));
    }
}

