package com.rafael.nailspro.webapp.infrastructure.dto.salon.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;
import java.util.Optional;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SalonServiceDTO(Long id,
                              String name,
                              Integer value,
                              Integer durationInSeconds,
                              String description,
                              Optional<List<Long>> professionals) {}
