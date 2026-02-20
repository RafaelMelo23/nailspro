package com.rafael.nailspro.webapp.infrastructure.controller.api.evolution.webhook;

import com.fasterxml.jackson.databind.JsonNode;
import com.rafael.nailspro.webapp.application.whatsapp.webhook.WebhookProcessorService;
import com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook.EvolutionWebhookResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/webhook")
public class WebhookController {

    private final WebhookProcessorService  webhookProcessorService;

    @PostMapping
    public ResponseEntity<Void> process(@RequestBody EvolutionWebhookResponse<?> dto) {

        webhookProcessorService.handleWebhook(dto);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONNECTION, "close")
                .build();
    }

    @PostMapping("/debug")
    public ResponseEntity<Void> webhookDebugger(@RequestBody JsonNode body) {
        String event = body.has("event") ? body.get("event").asText() : "unknown";

        log.info("Novo Webhook recebido - Evento: {}", event);
        log.info("Payload completo: {}", body.toPrettyString());


        return ResponseEntity.ok().build();
    }

}