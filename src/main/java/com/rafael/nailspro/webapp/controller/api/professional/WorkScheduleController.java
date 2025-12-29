package com.rafael.nailspro.webapp.controller.api.professional;

import com.rafael.nailspro.webapp.model.dto.professional.schedule.WorkScheduleRecordDTO;
import com.rafael.nailspro.webapp.model.entity.user.UserPrincipal;
import com.rafael.nailspro.webapp.service.professional.WorkScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedule")
public class WorkScheduleController {

    private final WorkScheduleService workScheduleService;

    @GetMapping
    public ResponseEntity<Set<WorkScheduleRecordDTO>> getSchedules(@AuthenticationPrincipal
                                                                       UserPrincipal userPrincipal) {

        return ResponseEntity.ok(workScheduleService.getWorkSchedules(userPrincipal.getUserId()));
    }

    @PostMapping
    public ResponseEntity<Void> createWorkSchedule(@RequestBody List<WorkScheduleRecordDTO> workScheduleRecordDTO,
                                                   @AuthenticationPrincipal UserPrincipal userPrincipal) {

        workScheduleService.registerSchedules(workScheduleRecordDTO, userPrincipal.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping
    public ResponseEntity<Void> modifySchedules(@RequestBody List<WorkScheduleRecordDTO> workScheduleRecordDTO,
                                                @AuthenticationPrincipal UserPrincipal userPrincipal) {

        workScheduleService.modifyWeekSchedule(workScheduleRecordDTO, userPrincipal.getUserId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {

        workScheduleService.deleteSchedule(scheduleId, userPrincipal.getUserId());
        return ResponseEntity.noContent().build();
    }
}
