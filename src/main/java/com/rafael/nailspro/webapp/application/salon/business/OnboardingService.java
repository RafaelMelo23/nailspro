package com.rafael.nailspro.webapp.application.salon.business;

import com.rafael.nailspro.webapp.domain.enums.UserRole;
import com.rafael.nailspro.webapp.domain.enums.UserStatus;
import com.rafael.nailspro.webapp.domain.model.Professional;
import com.rafael.nailspro.webapp.domain.model.SalonProfile;
import com.rafael.nailspro.webapp.domain.repository.ProfessionalRepository;
import com.rafael.nailspro.webapp.domain.repository.SalonProfileRepository;
import com.rafael.nailspro.webapp.infrastructure.dto.onboarding.OnboardingRequestDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.onboarding.OnboardingResultDTO;
import com.rafael.nailspro.webapp.infrastructure.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final ProfessionalRepository professionalRepository;
    private final SalonProfileRepository salonProfileRepository;
    private final PasswordEncoder passwordEncoder;

    // todo: remember to make the spring filters skip its API
    @Transactional
    public OnboardingResultDTO onboardOwner(OnboardingRequestDTO dto) {

        String sanitizedDomainSlug = buildSanitizedDomainSlug(dto);

        Professional salonOwner = professionalRepository.save(Professional.builder()
                .fullName(dto.fullName())
                .email(dto.email())
                .password(passwordEncoder.encode("mudar123"))
                .userRole(UserRole.ADMIN)
                .status(UserStatus.ACTIVE)
                .tenantId(sanitizedDomainSlug)
                .build());

        SalonProfile salonProfile = salonProfileRepository.save(SalonProfile.builder()
                .owner(salonOwner)
                .tenantId(sanitizedDomainSlug)
                .domainSlug(sanitizedDomainSlug)
                .build());

        return OnboardingResultDTO.builder()
                .profile(salonProfile)
                .owner(salonOwner)
                .build();
    }

    private static String buildSanitizedDomainSlug(OnboardingRequestDTO dto) {
        String sanitizedDomainSlug = dto.domainSlug()
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");

        if (sanitizedDomainSlug.isEmpty()) throw new BusinessException("Slug do domínio não pode estar vazia");
        return sanitizedDomainSlug;
    }
}
