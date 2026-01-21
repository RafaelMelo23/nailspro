package com.rafael.nailspro.webapp.application.whatsapp;

public interface WhatsappProvider {

    String createInstance(String tenantId);
    String connectInstanceViaPhoneNumber(String instanceName, String phoneNumber);
    String connectInstanceViaQrCode(String instanceName, String phoneNumber);
    void sendText(String instanceName, String message, String targetNumber);
    void logout(String instanceName);
}
