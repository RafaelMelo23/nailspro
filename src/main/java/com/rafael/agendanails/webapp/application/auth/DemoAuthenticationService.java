package com.rafael.agendanails.webapp.application.auth;

import com.rafael.agendanails.webapp.domain.model.Professional;
import com.rafael.agendanails.webapp.domain.repository.UserRepository;
import com.rafael.agendanails.webapp.infrastructure.dto.auth.AuthResultDTO;
import com.rafael.agendanails.webapp.infrastructure.dto.auth.LoginDTO;
import com.rafael.agendanails.webapp.shared.tenant.IgnoreTenantFilter;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class DemoAuthenticationService {

    @Value("${demo.tenant}")
    private String demoTenant;

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @IgnoreTenantFilter
    public AuthResultDTO createAndLoginDemoAdmin() {
        var demoProfessional = createDemoProfessional();

        userRepository.save(demoProfessional);
        entityManager.flush();

        return authenticationService.login(
                new LoginDTO(demoProfessional.getEmail(), "123456")
        );
    }

    private static int generateDemoNumber() {
        return 1000 + ThreadLocalRandom.current().nextInt(9000);
    }

    public Professional createDemoProfessional() {
        int number = generateDemoNumber();

        String name = "Profissional Demo " + number;
        String email = "demo.profissional" + number + "@nailspace.com";
        String hashedPassword = passwordEncoder.encode("123456");

        Professional adminProfessional = Professional.createAdminProfessional(
                name,
                email);

        adminProfessional.setPassword(hashedPassword);
        adminProfessional.setTenantId(demoTenant);
        adminProfessional.setIsFirstLogin(false);
        return adminProfessional;
    }
}