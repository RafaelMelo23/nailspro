package com.rafael.nailspro.webapp.service.admin.professional;

import com.rafael.nailspro.webapp.model.dto.admin.professional.CreateProfessionalDTO;
import com.rafael.nailspro.webapp.model.dto.salon.service.SalonServiceDTO;
import com.rafael.nailspro.webapp.model.entity.SalonService;
import com.rafael.nailspro.webapp.model.entity.user.Professional;
import com.rafael.nailspro.webapp.model.enums.UserRole;
import com.rafael.nailspro.webapp.model.repository.ProfessionalRepository;
import com.rafael.nailspro.webapp.service.SalonServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminProfessionalService {

    private final ProfessionalRepository professionalRepository;
    private final SalonServiceService salonServiceService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createProfessional(CreateProfessionalDTO professionalDTO) {

        Set<SalonService> servicesOfferedByProfessional =
                new HashSet<>(salonServiceService.findByIdIn(
                        professionalDTO.servicesOfferedByProfessional()
                                .stream()
                                .toList()
                ));

        professionalRepository.save(Professional.builder()
                .salonServices(servicesOfferedByProfessional)
                .fullName(professionalDTO.fullName())
                .email(professionalDTO.email())
                .password(passwordEncoder.encode("mudar123"))
                .userRole(UserRole.PROFESSIONAL)
                .build());
    }

    @Transactional
    public void deactivateProfessional(Long id) {

        professionalRepository.deactivateProfessional(id);
    }
}
