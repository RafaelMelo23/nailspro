package com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook;

public record MessageUpdateData(
        String messageId,
        String keyId,
        String remoteJid,
        boolean fromMe,
        String participant,
        String status, // PODE SER: PENDING, SERVER_ACK, DELIVERY_ACK, READ, PLAYED
        String instanceId
) {}


