package com.rafael.nailspro.webapp.infrastructure.dto.appointment.date;

import com.rafael.nailspro.webapp.infrastructure.dto.appointment.contract.BusyInterval;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record SimpleBusyInterval(LocalTime start,
                                 LocalTime end) implements BusyInterval {

    @Override
    public LocalTime getStart() {
        return start();
    }

    @Override
    public LocalTime getEnd() {
        return end();
    }
}
