package com.rafael.nailspro.webapp.model.enums.security;

import lombok.Getter;

@Getter
public enum TokenClaim {

    ID("id"),
    ROLE("role");

    private final String value;

    TokenClaim(String value) {
        this.value = value;
    }
}
