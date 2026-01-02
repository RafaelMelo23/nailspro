package com.rafael.nailspro.webapp.infrastructure.dto.onboarding;

import lombok.Builder;

@Builder
public record OnboardingRequestDTO(String fullName,
                                   String email,
                                   String domainSlug
                                   ) {
}
