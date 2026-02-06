package com.rafael.nailspro.webapp.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EvolutionEvent {
    // Eventos de Sistema
    APPLICATION_STARTUP("APPLICATION_STARTUP"),

    // Eventos de Conexão e Auth
    QRCODE_UPDATED("QRCODE_UPDATED"), // check
    CONNECTION_UPDATE("CONNECTION_UPDATE"),

    // MESSAGES_UPDATE("MESSAGES_UPDATE"), // Status (lida, entregue)
    SEND_MESSAGE("SEND_MESSAGE");

    private final String value;

    EvolutionEvent(String value) {
        this.value = value;
    }

    // Essa anotação faz o JSON serializar o valor da String ("QRCODE_UPDATED")
    // e não o objeto Enum em si
    @JsonValue
    public String getValue() {
        return value;
    }
}
