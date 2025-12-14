package com.rafael.nailspro.webapp.service;

import com.rafael.nailspro.webapp.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    public void register() {


    }
}
