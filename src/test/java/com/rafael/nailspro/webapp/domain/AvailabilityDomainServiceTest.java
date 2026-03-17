package com.rafael.nailspro.webapp.domain;

import com.rafael.nailspro.webapp.domain.repository.AppointmentRepository;
import com.rafael.nailspro.webapp.domain.repository.ProfessionalRepository;
import com.rafael.nailspro.webapp.domain.repository.ScheduleBlockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AvailabilityDomainServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private ScheduleBlockRepository scheduleBlockRepository;
    @Mock
    private ProfessionalRepository professionalRepository;

    @InjectMocks
    private AvailabilityDomainService availabilityDomainService;

    @Test
    void placeholder() {
        assertTrue(true);
        // TODO: add tests for slot calculation, lunch break, and buffers
    }
}