package com.rafael.nailspro.webapp.application.whatsapp.webhook;

import com.rafael.nailspro.webapp.application.professional.ProfessionalQueryService;
import com.rafael.nailspro.webapp.application.sse.EvolutionConnectionNotificationService;
import com.rafael.nailspro.webapp.domain.enums.EvolutionEvent;
import com.rafael.nailspro.webapp.domain.model.Professional;
import com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook.EvolutionWebhookResponse;
import com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook.QrCodeData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QrCodeUpdatedUseCase implements WebhookStrategy {

    private final ProfessionalQueryService professionalQueryService;
    private final EvolutionConnectionNotificationService connectionNotificationService;

    @Override
    public void process(Object payload) {
        if (payload instanceof EvolutionWebhookResponse<?> evolutionWebhookResponse &&
                evolutionWebhookResponse.T() instanceof QrCodeData qrCodeData) {

            String tenantId = evolutionWebhookResponse.instance();
            Professional salonOwner = professionalQueryService.findByTenantId(tenantId);

            connectionNotificationService.buildAndSend(salonOwner.getId(), qrCodeData);
        }
    }

    @Override
    public EvolutionEvent getSupportedTypeEvent() {
        return EvolutionEvent.QRCODE_UPDATED;
    }
}
