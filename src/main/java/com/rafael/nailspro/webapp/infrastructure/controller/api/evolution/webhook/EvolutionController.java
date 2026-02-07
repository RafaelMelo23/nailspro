package com.rafael.nailspro.webapp.infrastructure.controller.api.evolution.webhook;

import com.rafael.nailspro.webapp.infrastructure.whatsapp.evolution.EvolutionWhatsappService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/evolution")
public class EvolutionController {

    private final EvolutionWhatsappService evolutionService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> sendMessage(
            @RequestParam String message,
            @RequestParam String targetNumber,
            @RequestParam String instanceName) {



        return ResponseEntity.ok(evolutionService.sendText(instanceName, message, targetNumber));
    }

}
