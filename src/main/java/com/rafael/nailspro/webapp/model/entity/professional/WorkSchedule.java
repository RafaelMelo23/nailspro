package com.rafael.nailspro.webapp.model.entity.professional;

import com.rafael.nailspro.webapp.model.dto.professional.schedule.WorkScheduleRecordDTO;
import com.rafael.nailspro.webapp.model.entity.user.Professional;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;


@Entity
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "work_schedule",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_professional_day",
                        columnNames = {"professional_id", "day_of_week"}
                )
        }
)
public class WorkSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "externalId", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime workStart;

    @Column(name = "end_time", nullable = false)
    private LocalTime workEnd;

    @Column(name = "lunch_break_start_time", nullable = false)
    private LocalTime lunchBreakStartTime;

    @Column(name = "lunch_break_end_time", nullable = false)
    private LocalTime lunchBreakEndTime;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

    public void updateFromDto(WorkScheduleRecordDTO dto) {
        if (dto.dayOfWeek() != null) this.dayOfWeek = dto.dayOfWeek();
        if (dto.startTime() != null) this.workStart = dto.startTime();
        if (dto.endTime() != null) this.workEnd = dto.endTime();
        if (dto.lunchBreakStartTime() != null) this.lunchBreakStartTime = dto.lunchBreakStartTime();
        if (dto.lunchBreakEndTime() != null) this.lunchBreakEndTime = dto.lunchBreakEndTime();
        if (dto.isActive() != null) this.isActive = dto.isActive();
    }

    public void validateUnregisteredDays(List<DayOfWeek> daysFromList) {


    }
}