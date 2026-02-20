package com.rafael.nailspro.webapp.infrastructure.controller.api.sse;

import com.rafael.nailspro.webapp.domain.model.UserPrincipal;
import com.rafael.nailspro.webapp.infrastructure.sse.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class SseController {

    private final SseService sseService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal.getUserId();

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        sseService.addEmitter(userId, emitter);

        try {
            emitter.send(SseEmitter.event()
                    .name("INIT")
                    .data("Connection established for user: " + userId));
        } catch (IOException e) {
            sseService.removeEmitter(userId);
        }

        return emitter;
    }
}