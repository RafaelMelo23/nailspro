package com.rafael.nailspro.webapp.infrastructure.controller.api.admin;

import com.rafael.nailspro.webapp.application.salon.business.OnboardingService;
import com.rafael.nailspro.webapp.infrastructure.dto.onboarding.OnboardingRequestDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.onboarding.OnboardingResultDTO;
import com.rafael.nailspro.webapp.infrastructure.exception.BusinessException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/onboard")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class TenantOnboardingController {

    private final OnboardingService onboardingService;

    @PostMapping
    public ResponseEntity<OnboardingResultDTO> onboardNewClient(@Valid @RequestBody OnboardingRequestDTO dto) {

        return ResponseEntity.ok(onboardingService.onboardOwner(dto));
    }
}