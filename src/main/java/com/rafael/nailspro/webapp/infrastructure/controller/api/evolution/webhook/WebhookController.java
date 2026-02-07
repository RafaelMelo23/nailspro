package com.rafael.nailspro.webapp.infrastructure.controller.api.evolution.webhook;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/webhook")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    // mapear os webhooks necessarios (no enum sao os que vou utilizar)

    @PostMapping
    public ResponseEntity<Void> answerWebhook(@RequestBody JsonNode body) {
        // Identifica o tipo de evento (ex: messages.upsert, connection.update)
        String event = body.has("event") ? body.get("event").asText() : "unknown";

        logger.info("Novo Webhook recebido - Evento: {}", event);
        logger.info("Payload completo: {}", body.toPrettyString());

        // Sugestão de fluxo básico para processar as mensagens
        if ("messages.upsert".equals(event)) {
            processMessage(body.get("data"));
        }

        return ResponseEntity.ok().build();
    }

    private void processMessage(JsonNode data) {
        // Na v2, o texto costuma estar em message.conversation ou message.extendedTextMessage.text
        logger.info("Processando dados da mensagem...");
    }
}