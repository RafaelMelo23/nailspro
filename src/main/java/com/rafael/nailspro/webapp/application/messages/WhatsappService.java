package com.rafael.nailspro.webapp.application.messages;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WhatsappService {

    private final RestTemplate restTemplate;


}
