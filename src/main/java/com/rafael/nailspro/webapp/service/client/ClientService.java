package com.rafael.nailspro.webapp.service.client;

import com.rafael.nailspro.webapp.model.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional
    public void incrementClientCancelledAppointments(Long clientId) {

        clientRepository.incrementCanceledAppointments(clientId);
    }
}
