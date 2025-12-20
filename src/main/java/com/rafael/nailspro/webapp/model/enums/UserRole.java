package com.rafael.nailspro.webapp.model.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UserRole {

    ADMIN("admin"),
    PROFESSIONAL("professional"),
    CLIENT("client");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public static UserRole fromString(String role) {

        return Arrays.stream(values())
                .filter(r -> r.role.equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid role: " + role));
    }
}
