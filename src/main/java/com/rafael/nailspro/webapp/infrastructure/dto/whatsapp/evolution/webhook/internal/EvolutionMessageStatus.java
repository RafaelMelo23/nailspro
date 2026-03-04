package com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook.internal;

import com.rafael.nailspro.webapp.domain.enums.evolution.EvolutionWebhookEvent;

public record EvolutionMessageStatus(
        EvolutionWebhookEvent event,
        String instance,
        String messageId,
        String status
) {}
