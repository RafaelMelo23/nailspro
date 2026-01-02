package com.rafael.nailspro.webapp.infrastructure.dto.onboarding;

import com.rafael.nailspro.webapp.domain.profile.SalonProfile;
import com.rafael.nailspro.webapp.domain.user.Professional;
import lombok.Builder;

@Builder
public record OnboardingResultDTO(SalonProfile profile,
                                  Professional owner) {
}
