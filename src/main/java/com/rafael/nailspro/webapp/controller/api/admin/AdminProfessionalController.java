package com.rafael.nailspro.webapp.controller.api.admin;

import com.rafael.nailspro.webapp.model.dto.admin.professional.CreateProfessionalDTO;
import com.rafael.nailspro.webapp.model.dto.professional.schedule.WorkScheduleRecordDTO;
import com.rafael.nailspro.webapp.model.dto.professional.schedule.block.ScheduleBlockOutDTO;
import com.rafael.nailspro.webapp.model.entity.user.Professional;
import com.rafael.nailspro.webapp.service.professional.ProfessionalService;
import com.rafael.nailspro.webapp.service.admin.professional.AdminProfessionalService;
import com.rafael.nailspro.webapp.service.professional.ScheduleBlockService;
import com.rafael.nailspro.webapp.service.professional.WorkScheduleService;
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
public class AdminProfessionalController {

    private final AdminProfessionalService adminProfessionalService;
    private final ProfessionalService professionalService;
    private final WorkScheduleService workScheduleService;
    private final ScheduleBlockService scheduleBlockService;

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

