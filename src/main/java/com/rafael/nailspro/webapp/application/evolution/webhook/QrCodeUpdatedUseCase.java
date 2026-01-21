package com.rafael.nailspro.webapp.application.evolution.webhook;

import com.rafael.nailspro.webapp.application.professional.ProfessionalReadService;
import com.rafael.nailspro.webapp.application.sse.EvolutionConnectionNotificationService;
import com.rafael.nailspro.webapp.domain.enums.EvolutionEvent;
import com.rafael.nailspro.webapp.domain.tenant.TenantInstance;
import com.rafael.nailspro.webapp.domain.user.Professional;
import com.rafael.nailspro.webapp.domain.webhook.WebhookStrategy;
import com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook.QrCodeData;
import com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook.WebhookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QrCodeUpdatedUseCase implements WebhookStrategy {

    private final ProfessionalReadService professionalReadService;
    private final EvolutionConnectionNotificationService connectionNotificationService;

    @Override
    public void process(Object payload) {
        if (payload instanceof WebhookResponse<?> webhookResponse &&
                webhookResponse.T() instanceof QrCodeData qrCodeData) {

            String tenantId = TenantInstance.fromRawString(webhookResponse.instance());
            Professional salonOwner = professionalReadService.findByTenantId(tenantId);

            connectionNotificationService.buildAndSend(salonOwner.getId(), qrCodeData);
        }
    }

    @Override
    public EvolutionEvent getSupportedTypeEvent() {
        return EvolutionEvent.QRCODE_UPDATED;
    }
}
