package com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rafael.nailspro.webapp.domain.enums.EvolutionEvent;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateInstanceRequestDTO(String instanceName,
                                       String token,
                                       boolean qrcode,
                                       String number,
                                       String webhook,
                                       @JsonProperty("webhook_by_events")
                                       boolean webhookByEvents,
                                       List<EvolutionEvent> events) {
}
