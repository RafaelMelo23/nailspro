package com.rafael.nailspro.webapp.application.sse;

import com.rafael.nailspro.webapp.domain.enums.SseEventType;
import com.rafael.nailspro.webapp.infrastructure.dto.sse.SsePayloadDTO;
import com.rafael.nailspro.webapp.infrastructure.sse.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EvolutionConnectionNotificationService {

    private final SseService sseService;

    public void buildAndSend(Long professionalId, Object data) {

        SsePayloadDTO payload = SsePayloadDTO.builder()
                .sseEventType(SseEventType.QR_CODE_UPDATE)
                .data(data)
                .build();

        sseService.sendEventToUser(professionalId, payload);
    }
}
