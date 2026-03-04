package com.rafael.nailspro.webapp.application.whatsapp;

import java.util.Optional;

public interface WhatsappProvider {

    String createInstance(String tenantId);
    void deleteInstance(String instanceId);
    String instanceConnect(String instanceName, Optional<String> phoneNumber);
    void sendText(String tenantId, String message, String targetNumber);
    void logout(String instanceName);
}
