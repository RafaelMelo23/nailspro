package com.rafael.nailspro.webapp.model.dto.admin.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rafael.nailspro.webapp.model.enums.UserStatus;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClientDTO(
        Long clientId,
        String fullName,
        String email,
        String phoneNumber,
        Integer missedAppointments,
        UserStatus userStatus
) {
}
