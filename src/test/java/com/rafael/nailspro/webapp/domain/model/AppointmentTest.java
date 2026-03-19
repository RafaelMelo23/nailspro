package com.rafael.nailspro.webapp.domain.model;

import com.rafael.nailspro.webapp.domain.enums.appointment.AppointmentStatus;
import com.rafael.nailspro.webapp.infrastructure.exception.BusinessException;
import com.rafael.nailspro.webapp.support.factory.TestAppointmentAddOnFactory;
import com.rafael.nailspro.webapp.support.factory.TestAppointmentFactory;
import com.rafael.nailspro.webapp.support.factory.TestProfessionalFactory;
import com.rafael.nailspro.webapp.support.factory.TestSalonServiceFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AppointmentTest {

    private static Appointment buildAppointment(AppointmentStatus status) {
        return TestAppointmentFactory.atSpecificTime(
                Instant.now().minusSeconds(1800),
                Instant.now().plusSeconds(1800),
                TestProfessionalFactory.standard(),
                status
        );
    }

    private static Appointment finishedAppointment(AppointmentStatus status) {
        return TestAppointmentFactory.atSpecificTime(
                Instant.now().minusSeconds(7200),
                Instant.now().minusSeconds(3600),
                TestProfessionalFactory.standard(),
                status
        );
    }

    @Test
    void miss_whenAppointmentHasFinalStatus_shouldThrowBusinessException() {
        Appointment appointment = buildAppointment(AppointmentStatus.FINISHED);

        assertThrows(BusinessException.class, appointment::miss);
    }

    @Test
    void miss_whenAppointmentNotFinished_shouldThrowBusinessException() {
        Appointment appointment = buildAppointment(AppointmentStatus.CONFIRMED);

        assertThrows(BusinessException.class, appointment::miss);
    }

    @Test
    void miss_whenAppointmentFinished_shouldSetStatusToMissed() {
        Appointment appointment = finishedAppointment(AppointmentStatus.CONFIRMED);

        appointment.miss();

        assertEquals(AppointmentStatus.MISSED, appointment.getAppointmentStatus());
    }

    @Test
    void cancel_whenAppointmentHasFinalStatus_shouldThrowBusinessException() {
        Appointment appointment = buildAppointment(AppointmentStatus.FINISHED);

        assertThrows(BusinessException.class, appointment::cancel);
    }

    @Test
    void cancel_whenAppointmentActive_shouldSetStatusToCancelled() {
        Appointment appointment = buildAppointment(AppointmentStatus.CONFIRMED);

        appointment.cancel();

        assertEquals(AppointmentStatus.CANCELLED, appointment.getAppointmentStatus());
    }

    @Test
    void finish_whenAppointmentHasFinalStatus_shouldThrowBusinessException() {
        Appointment appointment = buildAppointment(AppointmentStatus.FINISHED);

        assertThrows(BusinessException.class, appointment::finish);
    }

    @Test
    void finish_whenAppointmentNotFinished_shouldThrowBusinessException() {
        Appointment appointment = buildAppointment(AppointmentStatus.CONFIRMED);

        assertThrows(BusinessException.class, appointment::finish);
    }

    @Test
    void finish_whenAppointmentFinished_shouldSetStatusToFinished() {
        Appointment appointment = finishedAppointment(AppointmentStatus.CONFIRMED);

        appointment.finish();

        assertEquals(AppointmentStatus.FINISHED, appointment.getAppointmentStatus());
    }

    @Test
    void confirm_whenAppointmentNotConfirmed_shouldSetStatusToConfirmed() {
        Appointment appointment = buildAppointment(AppointmentStatus.PENDING);

        appointment.confirm();

        assertEquals(AppointmentStatus.CONFIRMED, appointment.getAppointmentStatus());
    }

    @Test
    void confirm_whenAlreadyConfirmed_shouldDoNothing() {
        Appointment appointment = buildAppointment(AppointmentStatus.CONFIRMED);

        appointment.confirm();

        assertEquals(AppointmentStatus.CONFIRMED, appointment.getAppointmentStatus());
    }

    @Test
    void calculateTotalValue_calculateTotalValue_shouldReturnCorrectValue() {
        SalonService salonService = TestSalonServiceFactory.standard(); // value: 50
        SalonService addOn = TestSalonServiceFactory.addOnWithoutMaintenanceInterval(); // value: 15
        AppointmentAddOn appointmentAddOn = TestAppointmentAddOnFactory.standard(addOn);

        Appointment appointment = Appointment.builder()
                .addOns(List.of(appointmentAddOn))
                .mainSalonService(salonService)
                .build();

        BigDecimal totalValue = appointment.calculateTotalValue();

        assertEquals(new BigDecimal(65), totalValue);
    }

    @Test
    void calculateTotalValue_calculateWithNullService_shouldThrowIllegalStateException() {
        Appointment appointment = Appointment.builder()
                .addOns(List.of())
                .mainSalonService(null)
                .build();

        assertThrows(IllegalStateException.class, appointment::calculateTotalValue);
    }

    @Test
    void calculateTotalValue_calculateWithNoAddOn_ReturnsMainServiceValue() {
        SalonService salonService = TestSalonServiceFactory.standard();

        Appointment appointment = Appointment.builder()
                .addOns(List.of())
                .mainSalonService(salonService)
                .build();

        BigDecimal result = appointment.calculateTotalValue();

        assertEquals(0, result.compareTo(BigDecimal.valueOf(50)));
    }

    @Test
    void calculateTotalValue_WithMultipleAddOns_ReturnsSumOfAll() {
        SalonService main = TestSalonServiceFactory.standard(); // 50
        Appointment appointment = Appointment.builder()
                .mainSalonService(main)
                .addOns(List.of(
                        TestAppointmentAddOnFactory.standard(TestSalonServiceFactory.withCustomValue(5)),
                        TestAppointmentAddOnFactory.standard(TestSalonServiceFactory.withCustomValue(10)),
                        TestAppointmentAddOnFactory.standard(TestSalonServiceFactory.withCustomValue(15))
                ))
                .build();

        BigDecimal result = appointment.calculateTotalValue();

        assertEquals(0, result.compareTo(BigDecimal.valueOf(80)));
    }

    @Test
    void calculateTotalValue_WithZeroValueServices_ReturnsCorrectTotal() {
        SalonService main = TestSalonServiceFactory.withCustomValue(0);
        Appointment appointment = Appointment.builder()
                .mainSalonService(main)
                .addOns(List.of(
                        TestAppointmentAddOnFactory.standard(TestSalonServiceFactory.withCustomValue(0))
                ))
                .build();

        BigDecimal result = appointment.calculateTotalValue();

        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @Test
    void calculateTotalValue_WhenServiceValueIsNull_ThrowsIllegalStateException() {
        SalonService serviceNoValue = TestSalonServiceFactory.withCustomValue(null);

        Appointment appointment = Appointment.builder()
                .mainSalonService(serviceNoValue)
                .addOns(List.of())
                .build();

        assertThrows(IllegalStateException.class, appointment::calculateTotalValue);
    }

    @Test
    void calculateDurationInSeconds_WithMultipleAddOns_ReturnsCorrectTotalDuration() {
        SalonService main = TestSalonServiceFactory.withDuration(3600); // 1 hour
        List<AppointmentAddOn> addOns = List.of(
                TestAppointmentAddOnFactory.standard(TestSalonServiceFactory.withDuration(600)), // 10 mins
                TestAppointmentAddOnFactory.standard(TestSalonServiceFactory.withDuration(300))  // 5 mins
        );

        long totalDuration = Appointment.calculateDurationInSeconds(main, addOns);

        assertEquals(4500, totalDuration);
    }

    @Test
    void calculateDurationInSeconds_WithNullMainService_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                Appointment.calculateDurationInSeconds(null, List.of()));
    }

    @Test
    void calculateDurationInSeconds_WithNullAddOnsList_ReturnsOnlyMainServiceDuration() {
        SalonService main = TestSalonServiceFactory.withDuration(1800);

        long totalDuration = Appointment.calculateDurationInSeconds(main, null);

        assertEquals(1800, totalDuration);
    }

    @Test
    void calculateDurationInSeconds_WithEmptyAddOns_ReturnsOnlyMainServiceDuration() {
        SalonService main = TestSalonServiceFactory.withDuration(1800);

        long totalDuration = Appointment.calculateDurationInSeconds(main, List.of());

        assertEquals(1800, totalDuration);
    }

    @Test
    void calculateDurationInSeconds_WithNullEntriesInList_FiltersThemOut() {
        SalonService main = TestSalonServiceFactory.withDuration(3600);

        AppointmentAddOn validAddon = TestAppointmentAddOnFactory.standard(TestSalonServiceFactory.withDuration(600));
        AppointmentAddOn corruptedAddon = new AppointmentAddOn();

        List<AppointmentAddOn> addOns = new java.util.ArrayList<>();
        addOns.add(validAddon);
        addOns.add(null);
        addOns.add(corruptedAddon);

        long totalDuration = Appointment.calculateDurationInSeconds(main, addOns);

        assertEquals(4200, totalDuration);
    }
}