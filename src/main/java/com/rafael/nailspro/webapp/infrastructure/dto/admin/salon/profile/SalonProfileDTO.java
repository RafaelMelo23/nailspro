package com.rafael.nailspro.webapp.infrastructure.dto.admin.salon.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rafael.nailspro.webapp.domain.enums.OperationalStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SalonProfileDTO(String tradeName,
                              String slogan,
                              String primaryColor,
                              String logoBase64,
                              String comercialPhone,
                              String fullAddress,
                              String socialMediaLink,
                              OperationalStatus status,
                              String warningMessage,
                              Integer appointmentBufferMinutes) {
}
