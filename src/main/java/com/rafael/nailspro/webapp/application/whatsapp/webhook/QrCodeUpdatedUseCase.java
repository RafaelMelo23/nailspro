package com.rafael.nailspro.webapp.application.whatsapp.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafael.nailspro.webapp.application.professional.ProfessionalQueryService;
import com.rafael.nailspro.webapp.application.sse.EvolutionConnectionNotificationService;
import com.rafael.nailspro.webapp.domain.enums.evolution.EvolutionWebhookEvent;
import com.rafael.nailspro.webapp.domain.model.Professional;
import com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook.EvolutionWebhookResponse;
import com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook.QrCodeData;
import com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook.QrCodeDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QrCodeUpdatedUseCase implements WebhookStrategy {

    private final ProfessionalQueryService professionalQueryService;
    private final EvolutionConnectionNotificationService connectionNotificationService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void process(Object payload) {
        if (payload instanceof EvolutionWebhookResponse<?> evolutionWebhookResponse) {
            Object data = evolutionWebhookResponse.data();

            if (data instanceof java.util.Map) {
                data = objectMapper.convertValue(data, QrCodeData.class);
            }

            QrCodeDetails qrcodeDetails = ((QrCodeData) data).qrcode();

            String tenantId = evolutionWebhookResponse.instance();
            log.debug("Received QRCODE_UPDATED event for instance: {}", tenantId);

            Professional salonOwner = professionalQueryService.findByTenantId(tenantId);

            connectionNotificationService.notifyQrCodeUpdate(salonOwner.getId(), qrcodeDetails);
            log.info("New QR Code/Pairing Code pushed via SSE to Professional ID: {} (Instance: {})", salonOwner.getId(), tenantId);
        }
    }

    @Override
    public EvolutionWebhookEvent getSupportedTypeEvent() {
        return EvolutionWebhookEvent.QRCODE_UPDATED;
    }
}