package com.rafael.nailspro.webapp.model.dto.auth;

public record ResetPasswordDTO(String userEmail,
                               String newPassword,
                               String resetToken) {
}
