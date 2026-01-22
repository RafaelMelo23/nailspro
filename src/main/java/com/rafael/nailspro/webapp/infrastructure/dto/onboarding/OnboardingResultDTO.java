package com.rafael.nailspro.webapp.infrastructure.dto.onboarding;

import com.rafael.nailspro.webapp.domain.model.SalonProfile;
import com.rafael.nailspro.webapp.domain.model.Professional;
import lombok.Builder;

@Builder
public record OnboardingResultDTO(SalonProfile profile,
                                  Professional owner) {
}
