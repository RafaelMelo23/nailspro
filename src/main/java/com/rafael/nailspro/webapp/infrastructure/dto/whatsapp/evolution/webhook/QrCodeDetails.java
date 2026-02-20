package com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook;

public record QrCodeDetails(
        String instance,
        String pairingCode,
        String code,
        String base64
) {}