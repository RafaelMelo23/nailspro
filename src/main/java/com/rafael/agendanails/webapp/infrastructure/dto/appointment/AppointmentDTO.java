package com.rafael.agendanails.webapp.infrastructure.dto.appointment;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Builder
public record AppointmentDTO(
        Long id,
        String professionalName,
        String mainServiceName,
        BigDecimal totalValue,
        String observations,
        String status,
        ZonedDateTime startDate,
        ZonedDateTime endDate,
        String salonTradeName,
        String salonZoneId,
        List<AddOnDTO> addOns
) {}
