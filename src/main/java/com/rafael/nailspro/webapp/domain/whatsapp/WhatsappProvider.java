package com.rafael.nailspro.webapp.domain.whatsapp;

public interface WhatsappProvider {

    String createInstance(String tenantId);
    String connectInstanceViaPhoneNumber(String instanceName, String phoneNumber);
    String connectInstanceViaQrCode(String instanceName, String phoneNumber);
    void sendText(String instanceName, String message, String targetNumber);
    void logout(String instanceName);
}
