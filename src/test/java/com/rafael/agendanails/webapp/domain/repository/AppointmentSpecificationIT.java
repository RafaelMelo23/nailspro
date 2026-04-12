package com.rafael.agendanails.webapp.domain.repository;

import com.rafael.agendanails.webapp.domain.enums.appointment.AppointmentStatus;
import com.rafael.agendanails.webapp.domain.model.Appointment;
import com.rafael.agendanails.webapp.domain.model.Client;
import com.rafael.agendanails.webapp.domain.model.Professional;
import com.rafael.agendanails.webapp.domain.model.SalonService;
import com.rafael.agendanails.webapp.support.BaseIntegrationTest;
import com.rafael.agendanails.webapp.support.factory.TestAppointmentFactory;
import com.rafael.agendanails.webapp.support.factory.TestClientFactory;
import com.rafael.agendanails.webapp.support.factory.TestProfessionalFactory;
import com.rafael.agendanails.webapp.support.factory.TestSalonServiceFactory;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AppointmentSpecificationIT extends BaseIntegrationTest {

    private Professional professional;
    private Client client;
    private SalonService service;
    private final ZoneId zoneId = ZoneId.of("America/Sao_Paulo");

    @BeforeEach
    void setUp() {
        professional = professionalRepository.save(TestProfessionalFactory.standardForIt());
        client = clientRepository.save(TestClientFactory.standardForIt());
        service = salonServiceRepository.save(TestSalonServiceFactory.standardForIt());
    }

    @Test
    void withDate_shouldRespectBoundaryConditions() {
        LocalDate targetDate = LocalDate.of(2026, 4, 11);
        Instant startOfDay = targetDate.atStartOfDay(zoneId).toInstant();
        Instant endOfDay = targetDate.plusDays(1).atStartOfDay(zoneId).toInstant();

        Appointment atStart = TestAppointmentFactory.atSpecificTimeForIt(startOfDay, startOfDay.plus(1, ChronoUnit.HOURS), client, professional, service, AppointmentStatus.CONFIRMED);
        appointmentRepository.save(atStart);

        Appointment atNextDayStart = TestAppointmentFactory.atSpecificTimeForIt(endOfDay, endOfDay.plus(1, ChronoUnit.HOURS), client, professional, service, AppointmentStatus.CONFIRMED);
        appointmentRepository.save(atNextDayStart);

        Specification<Appointment> spec = AppointmentSpecification.withDate(targetDate, zoneId);
        List<Appointment> results = appointmentRepository.findAll(spec);

        assertThat(results).isNotEmpty();
        assertThat(results).anyMatch(ap -> ap.getId().equals(atStart.getId()));
        assertThat(results).noneMatch(ap -> ap.getId().equals(atNextDayStart.getId()));
    }

    @Test
    void withDateRange_shouldRespectBoundaryConditions() {
        ZonedDateTime start = ZonedDateTime.of(2026, 4, 11, 10, 0, 0, 0, zoneId);
        ZonedDateTime end = ZonedDateTime.of(2026, 4, 11, 11, 0, 0, 0, zoneId);

        Appointment atStart = TestAppointmentFactory.atSpecificTimeForIt(start.toInstant(), start.toInstant().plus(30, ChronoUnit.MINUTES), client, professional, service, AppointmentStatus.CONFIRMED);
        appointmentRepository.save(atStart);

        Appointment atEnd = TestAppointmentFactory.atSpecificTimeForIt(end.toInstant(), end.toInstant().plus(30, ChronoUnit.MINUTES), client, professional, service, AppointmentStatus.CONFIRMED);
        appointmentRepository.save(atEnd);

        Specification<Appointment> spec = AppointmentSpecification.withDateRange(start.toInstant(), end.toInstant());
        List<Appointment> results = appointmentRepository.findAll(spec);

        assertThat(results).isNotEmpty();
        assertThat(results).anyMatch(ap -> ap.getId().equals(atStart.getId()));
        assertThat(results).noneMatch(ap -> ap.getId().equals(atEnd.getId()));
    }

    @Test
    void fetchRelationships_shouldLoadAssociations() {
        Appointment appointment = TestAppointmentFactory.standardForIt(client, professional, service);
        appointmentRepository.save(appointment);

        entityManager.flush();
        entityManager.clear();

        Specification<Appointment> spec = AppointmentSpecification.fetchRelationships();
        List<Appointment> results = appointmentRepository.findAll(spec);

        assertThat(results).isNotEmpty();
        Appointment found = results.stream()
                .filter(ap -> ap.getId().equals(appointment.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(Hibernate.isInitialized(found.getClient())).isTrue();
        assertThat(Hibernate.isInitialized(found.getProfessional())).isTrue();
        assertThat(Hibernate.isInitialized(found.getMainSalonService())).isTrue();
    }
}
