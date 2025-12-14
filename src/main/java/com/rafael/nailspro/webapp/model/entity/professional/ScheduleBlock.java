package com.rafael.nailspro.webapp.model.entity.professional;

import com.rafael.nailspro.webapp.model.entity.user.Professional;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "schedule_block")
public class ScheduleBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "date_and_start_time", nullable = false)
    private LocalDateTime dateAndStartTime;

    @Column(name = "date_and_end_time", nullable = false)
    private LocalDateTime dateAndEndTime;

    @Column(name = "is_whole_day_blocked")
    private Boolean isWholeDayBlocked = Boolean.FALSE;

    @Column(name = "reason", nullable = false, length = 300)
    private String reason;

    @ManyToOne(optional = false)
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;
}