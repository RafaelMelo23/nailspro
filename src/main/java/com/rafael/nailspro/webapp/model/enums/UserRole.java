package com.rafael.nailspro.webapp.model.enums;

import lombok.Getter;

@Getter
public enum UserRole {

    ADMIN("admin"),
    PROFESSIONAL("professional"),
    CLIENT("client");

    private String role;

    UserRole(String role) {
        this.role = role;
    }
}
