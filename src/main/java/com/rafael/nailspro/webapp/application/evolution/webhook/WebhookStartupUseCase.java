package com.rafael.nailspro.webapp.application.evolution.webhook;

import com.rafael.nailspro.webapp.domain.enums.EvolutionEvent;
import com.rafael.nailspro.webapp.domain.webhook.WebhookStrategy;
import com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.EvolutionWebhookDTO;

public class WebhookStartupUseCase implements WebhookStrategy {

    @Override
    public void process(Object payload) {
        if (!(payload instanceof EvolutionWebhookDTO)) {
            return;
        }

        var webhookDTO = (EvolutionWebhookDTO) payload;

        // not sure this event exists
    }

    @Override
    public EvolutionEvent getSupportedTypeEvent() {
        return EvolutionEvent.APPLICATION_STARTUP;
    }
}
