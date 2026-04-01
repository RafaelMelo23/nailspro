package com.rafael.agendanails.webapp.infrastructure.dto.appointment;

import lombok.Builder;

@Builder
public record AddOnDTO(
        Long serviceId,
        String serviceName,
        Integer quantity,
        Integer unitPriceSnapshot
) {}
