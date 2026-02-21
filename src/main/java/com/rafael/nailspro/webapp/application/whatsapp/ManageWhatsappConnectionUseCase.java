package com.rafael.nailspro.webapp.application.whatsapp;

import com.rafael.nailspro.webapp.application.salon.business.SalonProfileService;
import com.rafael.nailspro.webapp.domain.enums.evolution.WhatsappConnectionMethod;
import com.rafael.nailspro.webapp.domain.model.SalonProfile;
import com.rafael.nailspro.webapp.infrastructure.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.rafael.nailspro.webapp.domain.enums.evolution.WhatsappConnectionMethod.PAIRING_CODE;

@Service
@RequiredArgsConstructor
public class ManageWhatsappConnectionUseCase {

    /*
        TODO: when making the front-end view for it. make it so the tenant can see the number that will be used for the connection (salon.comercialPhone),
        TODO: and make it so they can promptly change it if inadequate
     */

    private final SalonProfileService salonService;
    private final WhatsappProvider whatsappProvider;

    public void setupInitialConnection(String tenantId, WhatsappConnectionMethod connectionMethod) {

        try {
            whatsappProvider.deleteInstance(tenantId);
        } catch (Exception ignore) {
        }

        whatsappProvider.createInstance(tenantId);

        SalonProfile salon = salonService.findWithOwnerByTenantId(tenantId);
        salon.setWhatsappLastResetAt(LocalDateTime.now());

        String cleanPhoneNumber = basicPhoneFormatValidation(salon.getComercialPhone());
        salonService.save(salon);

        if (PAIRING_CODE.equals(connectionMethod)) {
            whatsappProvider.instanceConnect(tenantId, Optional.of(cleanPhoneNumber));
        }
    }

    private String basicPhoneFormatValidation(String phoneNumber) {
        if (phoneNumber == null) throw new BusinessException("Não há número registrado para realizar conexão.");

        String cleanNumber = phoneNumber.replaceAll("\\D", "");

        if (!cleanNumber.startsWith("55")) {
            cleanNumber = "55" + cleanNumber;
        }

        if (cleanNumber.length() < 12) throw new BusinessException("O número informado é inválido para conexão");

        return cleanNumber;
    }
}