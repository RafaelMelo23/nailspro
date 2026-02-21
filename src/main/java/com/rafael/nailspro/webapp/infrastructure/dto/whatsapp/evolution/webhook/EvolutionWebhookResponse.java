package com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rafael.nailspro.webapp.domain.enums.evolution.EvolutionWebhookEvent;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EvolutionWebhookResponse<T>(
        EvolutionWebhookEvent event,
        String instance,
        T data,
        String destination,
        @JsonProperty("date_time") String dateTime,
        @JsonProperty("server_url") String serverUrl,
        String sender,
        String apikey
) {}
