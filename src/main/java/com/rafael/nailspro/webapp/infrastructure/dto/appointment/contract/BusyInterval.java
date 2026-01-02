package com.rafael.nailspro.webapp.infrastructure.dto.appointment.contract;

import java.time.LocalTime;

public interface BusyInterval {

    LocalTime getStart();
    LocalTime getEnd();
}
