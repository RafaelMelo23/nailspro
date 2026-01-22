package com.rafael.nailspro.webapp.application.whatsapp.webhook;

import com.rafael.nailspro.webapp.domain.enums.EvolutionEvent;
import com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook.EvolutionWebhookResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WebhookProcessorService {

    private final Map<EvolutionEvent, WebhookStrategy> strategyMap;

    public WebhookProcessorService(List<WebhookStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(WebhookStrategy::getSupportedTypeEvent, Function.identity()));
    }

    public void handleWebhook(EvolutionWebhookResponse<?> webhookDTO) {

        WebhookStrategy handler = strategyMap.get(EvolutionEvent.valueOf(webhookDTO.event()));

        handler.process(webhookDTO);
    }
}
