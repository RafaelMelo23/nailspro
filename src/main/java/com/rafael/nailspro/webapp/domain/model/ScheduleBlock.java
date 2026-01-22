package com.rafael.nailspro.webapp.domain.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@SuperBuilder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedule_block")
public class ScheduleBlock extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "externalId", nullable = false)
    private Long id;

    @Column(name = "date_and_start_time", nullable = false)
    private Instant dateAndStartTime;

    @Column(name = "date_and_end_time", nullable = false)
    private Instant dateAndEndTime;

    @Column(name = "is_whole_day_blocked")
    private Boolean isWholeDayBlocked = Boolean.FALSE;

    @Column(name = "reason", nullable = false, length = 300)
    private String reason;

    @ManyToOne(optional = false)
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

}