package com.rafael.nailspro.webapp.application.whatsapp;

import java.util.Map;
import java.util.Optional;

public interface WhatsappProvider {

    String createInstance(String tenantId);
    String instanceConnect(String instanceName, Optional<String> phoneNumber);
    void sendTextVoid(String instanceName, String message, String targetNumber);
    Map<String, Object> sendText(String tenantId, String message, String targetNumber);
    void logout(String instanceName);
}
