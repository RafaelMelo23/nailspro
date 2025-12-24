package com.rafael.nailspro.webapp.model.dto.professional.schedule.block;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ScheduleBlockOutDTO(Long id,
                                  LocalDateTime dateAndStartTime,
                                  LocalDateTime dateAndEndTime,
                                  Boolean isWholeDayBlocked,
                                  String reason,
                                  Long professionalId) {
}
