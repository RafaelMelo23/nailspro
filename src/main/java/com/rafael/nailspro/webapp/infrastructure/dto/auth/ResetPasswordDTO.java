package com.rafael.nailspro.webapp.infrastructure.dto.auth;

public record ResetPasswordDTO(String userEmail,
                               String newPassword,
                               String resetToken) {
}
