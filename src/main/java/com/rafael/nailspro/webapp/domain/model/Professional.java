package com.rafael.nailspro.webapp.domain.model;

import com.rafael.nailspro.webapp.infrastructure.dto.appointment.contract.BusyInterval;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.date.SimpleBusyInterval;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;

//@SQLDelete(sql = "UPDATE professionals SET is_active = false WHERE user_id = ?")
//@FilterDef(
//        name = "activeFilter",
//        parameters = @ParamDef(name = "isActive", type = Boolean.class)
//)
//@Filter(name = "activeFilter", condition = "is_active = :isActive")
@Entity
@SuperBuilder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "professional")
@PrimaryKeyJoinColumn(name = "user_id")
public class Professional extends User {

    private String professionalPicture;

    @Column(unique = true, nullable = false)
    private UUID externalId = UUID.randomUUID();

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @Column(name = "is_first_login", nullable = false)
    private Boolean isFirstLogin = false;

    @OneToMany(mappedBy = "professional")
    private List<Appointment> professionalAppointments;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "professionals")
    private Set<SalonService> salonServices = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "professional", orphanRemoval = true)
    private Set<WorkSchedule> workSchedules = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "professional", orphanRemoval = true)
    private Set<ScheduleBlock> scheduleBlocks = new LinkedHashSet<>();

    @OneToOne(mappedBy = "owner", orphanRemoval = true)
    private SalonProfile salonProfile;

    public void prePersist() {
        this.isActive = Boolean.TRUE;
        this.isFirstLogin = Boolean.TRUE;
        this.externalId = UUID.randomUUID();
    }
}
