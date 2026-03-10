package com.rafael.nailspro.webapp.application.user;

import com.rafael.nailspro.webapp.domain.model.Client;
import com.rafael.nailspro.webapp.domain.model.User;
import com.rafael.nailspro.webapp.domain.repository.ClientRepository;
import com.rafael.nailspro.webapp.domain.repository.UserRepository;
import com.rafael.nailspro.webapp.infrastructure.dto.auth.ChangeEmailRequestDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.auth.ChangePhoneRequestDTO;
import com.rafael.nailspro.webapp.infrastructure.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileManagementUseCase {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    @Transactional
    public void updateEmail(Long userId, ChangeEmailRequestDTO request) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new BusinessException("Credenciais inválidas. Não foi possível alterar o email."));

            if (!passwordEncoder.matches(request.password(), user.getPassword())) {
                throw new BusinessException("Credenciais inválidas. Não foi possível alterar o email.");
            }

            if (userRepository.existsByEmail(request.newEmail())) {
                throw new BusinessException("Email já em uso.");
            }

            user.setEmail(request.newEmail());
            userRepository.save(user);

        } catch (Exception e) {
            throw new BusinessException("A operação falhou. Por favor, tente novamente mais tarde.");
        }
    }

    @Transactional
    public void updatePhone(Long clientId, ChangePhoneRequestDTO request) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new BusinessException("Credenciais inválidas. Não foi possível alterar o email."));

        if (!passwordEncoder.matches(request.password(), client.getPassword())) {
            throw new BusinessException("Credenciais inválidas. Não foi possível alterar o email.");
        }

        String cleanPhone = request.newPhone().replaceAll("\\D", "");

        if (clientRepository.existsByPhoneNumber(cleanPhone)) {
            throw new BusinessException("Este telefone já está vinculado a outra conta.");
        }

        client.setPhoneNumber(cleanPhone);
        clientRepository.save(client);
    }
}