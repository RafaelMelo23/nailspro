package com.rafael.nailspro.webapp.infrastructure.controller.api.professional;

import com.rafael.nailspro.webapp.infrastructure.dto.professional.schedule.block.ScheduleBlockDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.professional.schedule.block.ScheduleBlockOutDTO;
import com.rafael.nailspro.webapp.domain.user.UserPrincipal;
import com.rafael.nailspro.webapp.application.professional.ScheduleBlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedule/block")
public class ScheduleBlockController {

    private final ScheduleBlockService scheduleBlockService;

    @PostMapping
    public ResponseEntity<Void> createBlock(@RequestBody ScheduleBlockDTO blockDTO,
                                            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        scheduleBlockService.createBlock(blockDTO, userPrincipal.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{blockId}")
    public ResponseEntity<Void> deleteBlock(@PathVariable Long blockId,
                                            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        scheduleBlockService.deleteBlock(userPrincipal.getUserId(), blockId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{dateAndTime}")
    public ResponseEntity<List<ScheduleBlockOutDTO>> getBlocks(@PathVariable LocalDateTime dateAndTime,
                                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {

        return ResponseEntity.ok(scheduleBlockService.getBlocks(userPrincipal.getUserId(), Optional.of(dateAndTime)));
    }
}
