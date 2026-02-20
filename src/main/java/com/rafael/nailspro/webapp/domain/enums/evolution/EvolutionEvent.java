package com.rafael.nailspro.webapp.domain.enums.evolution;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EvolutionEvent {
    @JsonProperty("qrcode.updated")
    QRCODE_UPDATED,

    @JsonProperty("connection.update")
    CONNECTION_UPDATE,

    @JsonProperty("send.message")
    SEND_MESSAGE;
}