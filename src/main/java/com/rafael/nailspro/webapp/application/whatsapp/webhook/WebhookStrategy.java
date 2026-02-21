package com.rafael.nailspro.webapp.application.whatsapp.webhook;

import com.rafael.nailspro.webapp.domain.enums.evolution.EvolutionWebhookEvent;

public interface WebhookStrategy {

    void process(Object payload);

    EvolutionWebhookEvent getSupportedTypeEvent();
}
