package com.rafael.nailspro.webapp.model.dto.professional.schedule.block;

import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record ScheduleBlockOutDTO(Long id,
                                  ZonedDateTime dateAndStartTime,
                                  ZonedDateTime dateAndEndTime,
                                  Boolean isWholeDayBlocked,
                                  String reason,
                                  Long professionalId) {
}
