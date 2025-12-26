package com.rafael.nailspro.webapp.model.entity.user;

import com.rafael.nailspro.webapp.model.entity.Appointment;
import com.rafael.nailspro.webapp.model.entity.SalonService;
import com.rafael.nailspro.webapp.model.entity.professional.ScheduleBlock;
import com.rafael.nailspro.webapp.model.entity.professional.WorkSchedule;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import java.util.*;

//@SQLDelete(sql = "UPDATE professionals SET is_active = false WHERE user_id = ?")
//@FilterDef(
//        name = "activeFilter",
//        parameters = @ParamDef(name = "isActive", type = Boolean.class)
//)
//@Filter(name = "activeFilter", condition = "is_active = :isActive")
@Entity
@SuperBuilder
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "professionals")
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

    @ManyToMany(mappedBy = "professionals")
    private Set<SalonService> salonServices = new LinkedHashSet<>();

    @OneToMany(mappedBy = "professional", orphanRemoval = true)
    private Set<WorkSchedule> workSchedules = new HashSet<>();

    @OneToMany(mappedBy = "professional", orphanRemoval = true)
    private Set<ScheduleBlock> scheduleBlocks = new LinkedHashSet<>();

    @PrePersist
    public void prePersist() {
        this.isActive = Boolean.TRUE;
        this.isFirstLogin = Boolean.TRUE;
        this.externalId = UUID.randomUUID();
    }
}
