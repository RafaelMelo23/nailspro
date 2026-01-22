package com.rafael.nailspro.webapp.application.whatsapp.webhook;

import com.rafael.nailspro.webapp.application.professional.ProfessionalReadService;
import com.rafael.nailspro.webapp.application.sse.EvolutionConnectionNotificationService;
import com.rafael.nailspro.webapp.domain.enums.EvolutionEvent;
import com.rafael.nailspro.webapp.shared.tenant.TenantInstance;
import com.rafael.nailspro.webapp.domain.model.Professional;
import com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook.QrCodeData;
import com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook.EvolutionWebhookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QrCodeUpdatedUseCase implements WebhookStrategy {

    private final ProfessionalReadService professionalReadService;
    private final EvolutionConnectionNotificationService connectionNotificationService;

    @Override
    public void process(Object payload) {
        if (payload instanceof EvolutionWebhookResponse<?> evolutionWebhookResponse &&
                evolutionWebhookResponse.T() instanceof QrCodeData qrCodeData) {

            String tenantId = TenantInstance.fromRawString(evolutionWebhookResponse.instance());
            Professional salonOwner = professionalReadService.findByTenantId(tenantId);

            connectionNotificationService.buildAndSend(salonOwner.getId(), qrCodeData);
        }
    }

    @Override
    public EvolutionEvent getSupportedTypeEvent() {
        return EvolutionEvent.QRCODE_UPDATED;
    }
}
