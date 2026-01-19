package com.rafael.nailspro.webapp.application.evolution.webhook;

import com.rafael.nailspro.webapp.domain.enums.EvolutionEvent;
import com.rafael.nailspro.webapp.domain.webhook.WebhookStrategy;
import com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.EvolutionWebhookDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QrCodeUpdatedUseCase implements WebhookStrategy {

    @Override
    public void process(Object payload) {

        var webhookDTO = (EvolutionWebhookDTO) payload;


    }

    @Override
    public EvolutionEvent getSupportedTypeEvent() {
        return EvolutionEvent.QRCODE_UPDATED;
    }
}
