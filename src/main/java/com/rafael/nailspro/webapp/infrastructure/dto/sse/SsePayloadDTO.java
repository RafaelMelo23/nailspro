package com.rafael.nailspro.webapp.infrastructure.dto.sse;

import com.rafael.nailspro.webapp.domain.enums.SseEventType;
import lombok.Builder;

@Builder
public record SsePayloadDTO(SseEventType sseEventType,
                            Object data) {
}
