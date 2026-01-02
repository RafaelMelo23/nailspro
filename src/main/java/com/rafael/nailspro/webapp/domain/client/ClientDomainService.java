package com.rafael.nailspro.webapp.domain.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientDomainService {

    private final ClientRepository clientRepository;

    @Transactional
    public void incrementClientCancelledAppointments(Long clientId) {

        clientRepository.incrementCanceledAppointments(clientId);
    }
}
