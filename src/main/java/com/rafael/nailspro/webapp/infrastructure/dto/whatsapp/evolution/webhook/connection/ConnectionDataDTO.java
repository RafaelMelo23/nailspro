package com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook.connection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rafael.nailspro.webapp.domain.enums.evolution.EvolutionConnectionState;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConnectionDataDTO(
        EvolutionConnectionState state
) {}

