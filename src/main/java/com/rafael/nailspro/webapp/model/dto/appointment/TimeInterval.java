package com.rafael.nailspro.webapp.model.dto.appointment;

import com.rafael.nailspro.webapp.service.infra.exception.BusinessException;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record TimeInterval(LocalDateTime realStart,
                           LocalDateTime realEnd,
                           LocalDateTime endWithBuffer) {
    public TimeInterval {
        if (realEnd.isBefore(realStart)) {
            throw new BusinessException("O horário de término deve ser após o início.");
        }
    }

    public LocalTime getStartTimeOnly() {
        return realStart.toLocalTime();
    }

    public LocalTime getEndTimeOnly() {
        return realEnd.toLocalTime();
    }

    public DayOfWeek getDayOfWeek() {
        return realStart.getDayOfWeek();
    }
}
