package com.rafael.nailspro.webapp.application.auth;

import com.rafael.nailspro.webapp.domain.enums.user.UserStatus;
import com.rafael.nailspro.webapp.domain.model.Client;
import com.rafael.nailspro.webapp.domain.model.RefreshToken;
import com.rafael.nailspro.webapp.domain.model.User;
import com.rafael.nailspro.webapp.domain.repository.ClientRepository;
import com.rafael.nailspro.webapp.domain.repository.UserRepository;
import com.rafael.nailspro.webapp.infrastructure.dto.auth.AuthResultDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.auth.LoginDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.auth.RegisterDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.auth.TokenRefreshResponseDTO;
import com.rafael.nailspro.webapp.infrastructure.exception.BusinessException;
import com.rafael.nailspro.webapp.infrastructure.exception.TokenRefreshException;
import com.rafael.nailspro.webapp.infrastructure.exception.UserAlreadyExistsException;
import com.rafael.nailspro.webapp.infrastructure.security.token.RefreshTokenService;
import com.rafael.nailspro.webapp.infrastructure.security.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

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

    public AuthResultDTO login(LoginDTO loginDTO) {
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

        String jwt = tokenService.generateAuthToken(user);
        String refresh = refreshTokenService.createRefreshToken(user).getToken();

        return AuthResultDTO.builder()
                .jwtToken(jwt)
                .refreshToken(refresh).build();
    }

    @Transactional
    public TokenRefreshResponseDTO refreshToken(String refreshToken) {
        return refreshTokenService.findByToken(refreshToken)
                .map(token -> {
                    if (token.isRevoked()) {
                        refreshTokenService.revokeAllForUser(token.getUser().getId());
                        throw new TokenRefreshException("Este token já foi utilizado.");
                    }
                    return token;
                })
                .map(refreshTokenService::verifyExpiration)
                .map(token -> {
                    User user = token.getUser();
                    token.setRevoked(true);

                    RefreshToken newRefresh = refreshTokenService.createRefreshToken(user);
                    String newJwt = tokenService.generateAuthToken(user);

                    return new TokenRefreshResponseDTO(newJwt, newRefresh.getToken());
                })
                .orElseThrow(() -> new TokenRefreshException("Token não encontrado"));

    }
}