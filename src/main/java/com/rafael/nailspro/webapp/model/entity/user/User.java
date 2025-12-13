package com.rafael.nailspro.webapp.model.entity.user;

import com.rafael.nailspro.webapp.model.enums.UserStatus;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public abstract class User {
    @Id @GeneratedValue
    private Long id;
    private String fullName;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserStatus status;
}