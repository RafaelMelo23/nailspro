package com.rafael.nailspro.webapp.application.appointment;

import com.rafael.nailspro.webapp.domain.model.Appointment;
import com.rafael.nailspro.webapp.domain.model.AppointmentNotification;
import com.rafael.nailspro.webapp.domain.repository.AppointmentNotificationRepository;
import com.rafael.nailspro.webapp.domain.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.rafael.nailspro.webapp.domain.enums.appointment.AppointmentNotificationStatus.SENT;
import static com.rafael.nailspro.webapp.domain.enums.appointment.AppointmentNotificationType.REMINDER;

@Log4j2
@Component
@RequiredArgsConstructor
public class AppointmentReminderScheduler {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentNotificationRepository appointmentNotificationRepository;
    private final AppointmentMessagingUseCase messagingUseCase;

    @Scheduled(cron = "0 0/15 * * * *")
    @Transactional
    public void scheduleReminders() {
        log.info("Initiating search for appointment reminders");

        Instant windowStart = Instant.now();
        Instant nowPlusFiveHours = windowStart.plus(5, ChronoUnit.HOURS);

        List<Appointment> upcomingAppointments =
                appointmentRepository.findByStartDateBetween(windowStart, nowPlusFiveHours);

        getFilteredAppointments(upcomingAppointments)
                .forEach(ap -> messagingUseCase.sendAppointmentReminderMessage(ap.getId()));
    }

    private List<Appointment> getFilteredAppointments(List<Appointment> upcomingAppointments) {
        List<AppointmentNotification> appointmentNotifications =
                appointmentNotificationRepository.findByAppointments(upcomingAppointments);

        var reminderMap = appointmentNotifications.stream()
                .filter(ap -> REMINDER.equals(ap.getAppointmentNotificationType()))
                .collect(Collectors.toMap(
                        notif -> notif.getAppointment().getId(),
                        notif -> notif
                ));

        return upcomingAppointments.stream()
                .filter(ap -> shouldSendReminder(ap, reminderMap))
                .toList();
    }

    private boolean shouldSendReminder(Appointment ap, Map<Long, AppointmentNotification> reminderMap) {
        if (!reminderMap.containsKey(ap.getId())) {
            return true;
        }

        return reminderMap.get(ap.getId()).getAppointmentNotificationStatus() != SENT;
    }
}
