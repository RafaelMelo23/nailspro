package com.rafael.nailspro.webapp.model.dto.onboarding;

import com.rafael.nailspro.webapp.model.entity.salon.SalonProfile;
import com.rafael.nailspro.webapp.model.entity.user.Professional;
import lombok.Builder;

@Builder
public record OnboardingResultDTO(SalonProfile profile,
                                  Professional owner) {
}
