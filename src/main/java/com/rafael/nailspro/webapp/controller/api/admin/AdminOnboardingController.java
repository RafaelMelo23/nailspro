package com.rafael.nailspro.webapp.controller.api.admin;

import com.rafael.nailspro.webapp.model.dto.onboarding.OnboardingRequestDTO;
import com.rafael.nailspro.webapp.model.dto.onboarding.OnboardingResultDTO;
import com.rafael.nailspro.webapp.service.infra.exception.BusinessException;
import com.rafael.nailspro.webapp.service.salon.service.OnboardingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/onboard")
public class AdminOnboardingController {

    @Value("${internal.api.key}")
    private String key;

    private final OnboardingService onboardingService;

    @PostMapping
    public ResponseEntity<OnboardingResultDTO> onboardNewClient(@RequestBody OnboardingRequestDTO dto,
                                                                @RequestHeader ("X-API-Key") String apiKey) {

        if (!key.equals(apiKey)) throw new BusinessException("Invalid API Key");

        return ResponseEntity.ok(onboardingService.onboardOwner(dto));
    }
}
