package com.rafael.nailspro.webapp.application.whatsapp.webhook;

import com.rafael.nailspro.webapp.domain.enums.EvolutionEvent;

public interface WebhookStrategy {

    void process(Object payload);

    EvolutionEvent getSupportedTypeEvent();
}
