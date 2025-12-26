package com.rafael.nailspro.webapp.model.dto.appointment;

import com.rafael.nailspro.webapp.service.infra.exception.BusinessException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record TimeInterval(LocalDateTime start, LocalDateTime end) {
    public TimeInterval {
        if (end.isBefore(start)) {
            throw new BusinessException("O horário de término deve ser após o início.");
        }
    }

    public LocalTime getStartTimeOnly() {
        return start.toLocalTime();
    }

    public LocalTime getEndTimeOnly() {
        return end.toLocalTime();
    }

    public DayOfWeek getDayOfWeek() {
        return start.getDayOfWeek();
    }
}
