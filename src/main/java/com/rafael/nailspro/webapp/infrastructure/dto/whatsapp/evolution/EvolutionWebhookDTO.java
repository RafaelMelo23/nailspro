package com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EvolutionWebhookDTO(
        InstanceDTO instance,
        DataDTO data,
        String event
) {

    public record InstanceDTO(
            String instanceName,
            String status,
            Map<String, Object> extra
    ) {}

    public record DataDTO(
            KeyDTO key,
            MessageDTO message,
            UpdateDTO update,
            String state,
            String statusReason,
            String pushName,
            Long messageTimestamp,
            Map<String, Object> extra
    ) {}

    public record UpdateDTO(
        int status // 3 delivered, 4 read
    ) {}

    public record KeyDTO(
            String remoteJid,
            Boolean fromMe,
            String id
    ) {}

    public record MessageDTO(
            String conversation,
            Map<String, Object> extra
    ) {}
}

