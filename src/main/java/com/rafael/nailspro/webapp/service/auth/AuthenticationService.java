package com.rafael.nailspro.webapp.service.auth;

import com.rafael.nailspro.webapp.model.dto.auth.ChangeEmailRequestDTO;
import com.rafael.nailspro.webapp.model.dto.auth.LoginDTO;
import com.rafael.nailspro.webapp.model.dto.auth.RegisterDTO;
import com.rafael.nailspro.webapp.model.dto.auth.ResetPasswordDTO;
import com.rafael.nailspro.webapp.model.entity.user.Client;
import com.rafael.nailspro.webapp.model.entity.user.User;
import com.rafael.nailspro.webapp.model.enums.UserStatus;
import com.rafael.nailspro.webapp.model.repository.ClientRepository;
import com.rafael.nailspro.webapp.model.repository.UserRepository;
import com.rafael.nailspro.webapp.service.infra.email.EmailService;
import com.rafael.nailspro.webapp.service.infra.email.template.EmailTemplateBuilder;
import com.rafael.nailspro.webapp.service.infra.exception.BusinessException;
import com.rafael.nailspro.webapp.service.infra.exception.UserAlreadyExistsException;
import com.rafael.nailspro.webapp.service.infra.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public void register(RegisterDTO registerDTO) {
        checkIfUserAlreadyExists(registerDTO);

        String encryptedPassword = passwordEncoder.encode(registerDTO.rawPassword());

        clientRepository.save(Client.builder()
                .fullName(registerDTO.fullName())
                .email(registerDTO.email())
                .password(encryptedPassword)
                .status(UserStatus.ACTIVE)
                .phoneNumber(registerDTO.phoneNumber())
                .build()
        );
    }

    private void checkIfUserAlreadyExists(RegisterDTO registerDTO) {
        userRepository.findByEmailIgnoreCase(registerDTO.email())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("O E-mail informado já está sendo utilizado");
                });

        clientRepository.findByPhoneNumber(registerDTO.phoneNumber())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("O telefone informado já está sendo utilizado");
                });
    }

    public String login(LoginDTO loginDTO) {

        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(loginDTO.email());
        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!passwordEncoder.matches(loginDTO.password(), user.getPassword())) {
                throw new BusinessException("Os dados informados são inválidos");
            }
        } else {
            passwordEncoder.matches(loginDTO.password(), "$2a$10$C1J9Q8yCg.6r5PqW3H4Qz.pM3n5k7M9D2g6L1E8R0F4V7I0S2X9A1");

            throw new BusinessException("Os dados informados são inválidos");
        }

        return tokenService.generateAuthToken(user);
    }
}
