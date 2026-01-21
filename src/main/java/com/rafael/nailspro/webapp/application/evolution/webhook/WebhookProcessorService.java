package com.rafael.nailspro.webapp.application.evolution.webhook;

import com.rafael.nailspro.webapp.domain.enums.EvolutionEvent;
import com.rafael.nailspro.webapp.domain.webhook.WebhookStrategy;
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

    public void handleWebhook(EvolutionWebhookDTO webhookDTO) {

        WebhookStrategy handler = strategyMap.get(EvolutionEvent.valueOf(webhookDTO.event()));

        handler.process(webhookDTO);
    }
}
