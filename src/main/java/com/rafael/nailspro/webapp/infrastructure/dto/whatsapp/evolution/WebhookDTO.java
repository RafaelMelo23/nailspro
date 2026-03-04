package com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rafael.nailspro.webapp.domain.enums.evolution.EvolutionWebhookEvent;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record WebhookDTO(
        String url,
        boolean byEvents,
        boolean base64,
        List<EvolutionWebhookEvent> events
) {
}