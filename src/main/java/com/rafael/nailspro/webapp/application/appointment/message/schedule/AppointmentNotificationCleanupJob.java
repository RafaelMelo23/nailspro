package com.rafael.nailspro.webapp.application.appointment.message.schedule;

import com.rafael.nailspro.webapp.domain.repository.AppointmentNotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.rafael.nailspro.webapp.domain.enums.appointment.AppointmentNotificationStatus.SENT;

@Log4j2
@Service
@RequiredArgsConstructor
public class AppointmentNotificationCleanupJob {

    private final AppointmentNotificationRepository notificationRepository;

    @Scheduled(cron = "0 0 5 * * *")
    public void deleteSentNotifications() {
        Instant cutoff = Instant.now().minus(24, ChronoUnit.HOURS);
        try {
            int deleted =
                    notificationRepository.deleteByStatusAndSentAtSmallerThanInBatch(SENT, cutoff);

            if (deleted > 0) {
                log.info(
                        "Deleted {} SENT appointment notifications older than {}",
                        deleted,
                        cutoff
                );
            }
        } catch (Exception ex) {
            log.error("Failed to cleanup sent appointment notifications", ex);
        }
    }
}