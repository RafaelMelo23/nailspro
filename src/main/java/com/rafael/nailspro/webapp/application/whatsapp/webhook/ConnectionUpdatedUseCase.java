package com.rafael.nailspro.webapp.application.whatsapp.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafael.nailspro.webapp.application.salon.service.SalonProfileService;
import com.rafael.nailspro.webapp.application.sse.EvolutionConnectionNotificationService;
import com.rafael.nailspro.webapp.application.whatsapp.WhatsappProvider;
import com.rafael.nailspro.webapp.domain.enums.evolution.EvolutionEvent;
import com.rafael.nailspro.webapp.domain.model.SalonProfile;
import com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook.ConnectionData;
import com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.webhook.EvolutionWebhookResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.rafael.nailspro.webapp.domain.enums.evolution.EvolutionConnectionState.CLOSE;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionUpdatedUseCase implements WebhookStrategy {

    private final SalonProfileService salonProfileService;
    private final WhatsappProvider whatsappProvider;
    private final EvolutionConnectionNotificationService connectionNotificationService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void process(Object payload) {
        if (payload instanceof EvolutionWebhookResponse<?> response) {
            Object data = response.data();

            if (data instanceof java.util.Map) {
                data = objectMapper.convertValue(data, ConnectionData.class);
            }

            if (data instanceof ConnectionData connectionData) {
                String tenantId = connectionData.instance();
                log.info("Successfully converted LinkedHashMap to ConnectionData for instance: {}", response.instance());

                SalonProfile salon = salonProfileService.findWithOwnerByTenantId(tenantId);

                if (isUnderCooldown(salon)) {
                    log.warn("Ignoring close for tenant: {} - Instance is in pairing", tenantId);
                    return;
                }

                handleDisconnection(salon);
            }
        }
    }

    private boolean isUnderCooldown(SalonProfile salon) {
        if (salon.getWhatsappLastResetAt() == null) return false;

        return salon.getWhatsappLastResetAt()
                .isAfter(LocalDateTime.now().minusMinutes(2));
    }

    private void handleDisconnection(SalonProfile salon) {
        String tenantId = salon.getTenantId();
        Long ownerId = salon.getOwner().getId();

        salon.setEvolutionConnectionState(CLOSE);
        salon.setWhatsappLastResetAt(LocalDateTime.now());
        salonProfileService.save(salon);

        connectionNotificationService.notifyInstanceDisconnected(
                ownerId,
                "Whatsapp instance disconnected");

        log.info("SSE notification sent to owner ID: {} regarding instance {} disconnection.", ownerId, tenantId);

        whatsappProvider.deleteInstance(tenantId);
        log.info("Instance {} successfully deleted from Evolution API to prevent inconsistent connection states.", tenantId);
    }

    @Override
    public EvolutionEvent getSupportedTypeEvent() {
        return EvolutionEvent.CONNECTION_UPDATE;
    }
}