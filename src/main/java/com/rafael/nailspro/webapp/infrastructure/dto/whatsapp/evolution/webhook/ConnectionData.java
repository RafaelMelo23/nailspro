package com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook;

public record ConnectionData(
        String instance,
        String uid,
        String profileName,
        String profilePictureUrl,
        String state, // "open", "close", "connecting"
        int statusReason
) {}


