package com.rafael.nailspro.webapp.infrastructure.dto.auth;

import lombok.Builder;

@Builder
public record AuthResultDTO(String jwtToken,
                            String refreshToken) {
}
