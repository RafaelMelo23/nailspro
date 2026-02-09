package com.rafael.nailspro.webapp.domain.service;

import com.rafael.nailspro.webapp.application.service.SalonProfileService;
import com.rafael.nailspro.webapp.domain.model.Professional;
import com.rafael.nailspro.webapp.domain.model.SalonProfile;
import com.rafael.nailspro.webapp.domain.model.WorkSchedule;
import com.rafael.nailspro.webapp.domain.repository.ProfessionalRepository;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.AppointmentTimesDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.contract.BusyInterval;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AvailabilityDomainService {

    private final SalonProfileService salonProfileService;
    private final ProfessionalRepository professionalRepository;

    public Optional<AppointmentTimesDTO> findProfessionalDailyAvailability(Professional professional,
                                                                           LocalDate date,
                                                                           SalonProfile salonProfile,
                                                                           int serviceDurationInSeconds) {

        Optional<WorkSchedule> scheduleOpt = professional.getWorkSchedules().stream()
                .filter(ws -> ws.getDayOfWeek() == date.getDayOfWeek())
                .findFirst();

        if (scheduleOpt.isEmpty()) {
            return Optional.empty();
        }

        WorkSchedule schedule = scheduleOpt.get();
        List<BusyInterval> busyIntervals = professional.getBusyIntervals(
                date,
                salonProfile.getZoneId(),
                salonProfile.getAppointmentBufferMinutes()
        );

        List<LocalTime> availableTimes = new ArrayList<>();
        LocalTime cursor = schedule.getWorkStart();

        for (BusyInterval interval : busyIntervals) {
            if (cursor.isBefore(interval.getStart())) {
                processTimeSlotGaps(cursor, interval.getStart(), serviceDurationInSeconds, availableTimes);
            }

            if (interval.getEnd().isAfter(cursor)) {
                cursor = interval.getEnd();
            }
        }

        if (cursor.isBefore(schedule.getWorkEnd())) {
            processTimeSlotGaps(cursor, schedule.getWorkEnd(), serviceDurationInSeconds, availableTimes);
        }

        return Optional.of(new AppointmentTimesDTO(date, availableTimes));
    }

    private void processTimeSlotGaps(LocalTime gapStart, LocalTime gapEnd, int duration, List<LocalTime> suggestions) {
        final long LARGE_GAP_THRESHOLD = TimeUnit.HOURS.toSeconds(3);
        long gapSize = ChronoUnit.SECONDS.between(gapStart, gapEnd);

        if (gapSize < duration) return;

        suggestions.add(gapStart);
        LocalTime backLoadSlot = gapEnd.minusSeconds(duration);

        if (!backLoadSlot.equals(gapStart)) {
            suggestions.add(backLoadSlot);
        }

        if (gapSize >= LARGE_GAP_THRESHOLD) {
            LocalTime candidate = gapStart.plusHours(1);
            while (candidate.plusSeconds(duration).isBefore(gapEnd)) {
                if (!candidate.equals(backLoadSlot)) {
                    suggestions.add(candidate);
                }
                candidate = candidate.plusHours(1);
            }
        }
    }
}
