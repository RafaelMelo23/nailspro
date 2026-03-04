package com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.instance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rafael.nailspro.webapp.domain.enums.evolution.EvolutionIntegraton;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreateInstanceRequestDTO(
        String instanceName,
        boolean qrcode,
        EvolutionIntegraton integration,
        WebhookDTO webhook
) {
}