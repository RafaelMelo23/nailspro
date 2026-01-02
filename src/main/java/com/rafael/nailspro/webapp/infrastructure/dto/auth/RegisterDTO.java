package com.rafael.nailspro.webapp.infrastructure.dto.auth;

public record RegisterDTO(String fullName,
                          String email,
                          String rawPassword,
                          String phoneNumber
) {}
