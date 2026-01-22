package com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EvolutionWebhookResponse<T>(
        String event,
        String instance,
        Object T,
        String destination,
        @JsonProperty("date_time") String dateTime,
        @JsonProperty("server_url") String serverUrl,
        String sender,
        String apikey
) {}
