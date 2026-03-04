package com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook.connection.update;

public record QrCodeDetailsDTO(
        String pairingCode,
        String code,
        String base64
) {}