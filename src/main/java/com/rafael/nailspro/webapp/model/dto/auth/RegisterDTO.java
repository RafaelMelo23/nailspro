package com.rafael.nailspro.webapp.model.dto.auth;

public record RegisterDTO(String fullName,
                          String email,
                          String rawPassword,
                          String phoneNumber
) {}
