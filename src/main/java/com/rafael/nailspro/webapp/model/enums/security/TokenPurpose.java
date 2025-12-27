package com.rafael.nailspro.webapp.model.enums.security;

import lombok.Getter;

@Getter
public enum TokenPurpose {

    AUTHENTICATION("authentication"),
    RESET_PASSWORD("reset-password"),
    RESET_EMAIL("reset-email");

    private final String value;

    TokenPurpose(String value) {
        this.value = value;
    }
}
