package com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook;

public record QrCodeData(
        String qrcode,
        String base64,
        String pairingCode,
        int code
) {}

