package com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook.message.send;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rafael.nailspro.webapp.domain.enums.evolution.EvolutionMessageStatus;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SendMessageDTO(
        @JsonProperty("key")
        KeyDTO key,
        EvolutionMessageStatus status
) {
    public String messageId() {
        return key.id();
    }
}
