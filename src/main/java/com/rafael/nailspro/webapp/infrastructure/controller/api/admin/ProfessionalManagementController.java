package com.rafael.nailspro.webapp.infrastructure.controller.api.admin;

import com.rafael.nailspro.webapp.application.admin.professional.ProfessionalManagementService;
import com.rafael.nailspro.webapp.application.professional.ScheduleBlockService;
import com.rafael.nailspro.webapp.application.professional.WorkScheduleService;
import com.rafael.nailspro.webapp.domain.model.Professional;
import com.rafael.nailspro.webapp.domain.repository.ProfessionalRepository;
import com.rafael.nailspro.webapp.infrastructure.dto.admin.professional.CreateProfessionalDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.professional.schedule.WorkScheduleRecordDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.professional.schedule.block.ScheduleBlockOutDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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

    private final ProfessionalManagementService professionalManagementService;
    private final ProfessionalRepository professionalRepository;
    private final WorkScheduleService workScheduleService;
    private final ScheduleBlockService scheduleBlockService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createProfessional(
            @Valid @RequestBody CreateProfessionalDTO professionalDTO) {

        professionalManagementService.createProfessional(professionalDTO);
        return ResponseEntity.status(201).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateProfessional(
            @PathVariable Long id) {

        professionalManagementService.deactivateProfessional(id);
        return ResponseEntity.noContent().build();
    }

    //todo: perhaps change it to a more suitable DTO
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Professional>> getAllProfessionals() {

        return ResponseEntity.ok(professionalRepository.findAll());
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

