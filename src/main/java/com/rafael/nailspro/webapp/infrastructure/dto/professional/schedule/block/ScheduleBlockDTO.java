package com.rafael.nailspro.webapp.infrastructure.dto.professional.schedule.block;

import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record ScheduleBlockDTO(
        Long id,
        ZonedDateTime dateAndStartTime,
        ZonedDateTime dateAndEndTime,
        Boolean isWholeDayBlocked,
        String reason
) {}
