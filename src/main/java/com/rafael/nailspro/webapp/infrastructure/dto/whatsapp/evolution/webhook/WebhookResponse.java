package com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WebhookResponse<T>(
        String event,
        String instance,
        T data,
        String destination,
        @JsonProperty("date_time") String dateTime,
        @JsonProperty("server_url") String serverUrl,
        String sender,
        String apikey
) {}
