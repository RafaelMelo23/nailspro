package com.rafael.nailspro.webapp.domain.service;

import com.rafael.nailspro.webapp.domain.model.*;
import com.rafael.nailspro.webapp.domain.repository.AppointmentRepository;
import com.rafael.nailspro.webapp.domain.repository.ScheduleBlockRepository;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.AppointmentTimesDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.booking.AppointmentTimeWindow;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.contract.BusyInterval;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.date.SimpleBusyInterval;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.awt.SystemColor.window;

@Service
@RequiredArgsConstructor
public class AvailabilityDomainService {

    private final AppointmentRepository appointmentRepository;
    private final ScheduleBlockRepository scheduleBlockRepository;

    public List<AppointmentTimesDTO> findAvailableTimes(Professional professional,
                                                        AppointmentTimeWindow window,
                                                        SalonProfile salonProfile,
                                                        int serviceDurationInSeconds) {

        Map<LocalDate, List<BusyInterval>> intervalsByDate = getProfessionalBusyIntervals(
                professional, window.start(), window.end(), salonProfile)
                .stream()
                .collect(Collectors.groupingBy(BusyInterval::getDate));

        Map<DayOfWeek, WorkSchedule> weeklySchedules = professional.getWorkSchedules().stream()
                .collect(Collectors.toMap(WorkSchedule::getDayOfWeek, ws -> ws));

        List<AppointmentTimesDTO> results = new ArrayList<>();

        window.start().datesUntil(window.end()).forEach(date -> {

            WorkSchedule workSchedule = weeklySchedules.get(date.getDayOfWeek());

            if (workSchedule != null) {

                List<BusyInterval> dailyBusy = new ArrayList<>(intervalsByDate.getOrDefault(date, List.of()));

                if (workSchedule.getLunchBreakStartTime() != null
                        && workSchedule.getLunchBreakEndTime() != null) {

                    dailyBusy.add(SimpleBusyInterval.builder()
                            .start(workSchedule.getLunchBreakStartTime())
                            .end(workSchedule.getLunchBreakEndTime())
                            .date(date)
                            .build());
                }

                dailyBusy.sort(Comparator.comparing(BusyInterval::getStart));

                List<LocalTime> dailyAvailableTimes = new ArrayList<>();
                LocalTime cursor = workSchedule.getWorkStart();

                for (BusyInterval interval : dailyBusy) {
                    if (cursor.isBefore(interval.getStart())) {
                        processTimeSlotGaps(cursor, interval.getStart(), serviceDurationInSeconds, dailyAvailableTimes);
                    }

                    if (interval.getEnd().isAfter(cursor)) {
                        cursor = interval.getEnd();
                    }
                }

                if (cursor.isBefore(workSchedule.getWorkEnd())) {
                    processTimeSlotGaps(cursor, workSchedule.getWorkEnd(), serviceDurationInSeconds, dailyAvailableTimes);
                }

                results.add(new AppointmentTimesDTO(date, dailyAvailableTimes));
            }
        });

        return results;
    }

    private void processTimeSlotGaps(LocalTime gapStart, LocalTime gapEnd, int duration, List<LocalTime> suggestions) {
        long gapSize = ChronoUnit.SECONDS.between(gapStart, gapEnd);

        if (gapSize < duration) return;

        suggestions.add(gapStart);

        LocalTime candidate = gapStart.plusMinutes(30);

        while (candidate.plusSeconds(duration).isBefore(gapEnd)
                || candidate.plusSeconds(duration).equals(gapEnd)) {
            if (!suggestions.contains(candidate)) {
                suggestions.add(candidate);
            }

            candidate = candidate.plusMinutes(30);
        }
    }

    public List<BusyInterval> getProfessionalBusyIntervals(Professional professional,
                                                           LocalDate startRange,
                                                           LocalDate endRange,
                                                           SalonProfile salonProfile) {

        int salonBufferInMinutes = salonProfile.getAppointmentBufferMinutes();
        ZoneId zoneId = salonProfile.getZoneId();

        Instant instantStart = startRange.atStartOfDay(zoneId).toInstant();
        Instant instantEnd = endRange.atStartOfDay(zoneId).toInstant();

        List<Appointment> busyAppointments = appointmentRepository.findBusyAppointmentsInRange(professional.getId(), instantStart, instantEnd);
        List<ScheduleBlock> busyBlocks = scheduleBlockRepository.findBusyBlocksInRange(professional.getId(), instantStart, instantEnd);

        Stream<BusyInterval> appointmentStream = busyAppointments.stream()
                .map(ap -> SimpleBusyInterval.builder()
                        .start(LocalTime.ofInstant(ap.getStartDate(), zoneId))
                        .end(LocalTime.ofInstant(ap.getEndDate(), zoneId).plusMinutes(salonBufferInMinutes))
                        .date(LocalDate.ofInstant(ap.getStartDate(), zoneId))
                        .build());

        Stream<BusyInterval> blockStream = busyBlocks.stream()
                .map(sb -> SimpleBusyInterval.builder()
                        .start(LocalTime.ofInstant(sb.getDateStartTime(), zoneId))
                        .end(LocalTime.ofInstant(sb.getDateEndTime(), zoneId))
                        .date(LocalDate.ofInstant(sb.getDateStartTime(), zoneId))
                        .build());

        return Stream.concat(appointmentStream, blockStream)
                .sorted(Comparator.comparing(BusyInterval::getStart))
                .toList();
    }
}
