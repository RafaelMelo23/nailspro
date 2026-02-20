package com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rafael.nailspro.webapp.domain.enums.evolution.EvolutionConnectionState;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConnectionData(
        String instance,
        String uid,
        String profileName,
        String profilePictureUrl,
        EvolutionConnectionState state,
        Integer statusReason
) {}

