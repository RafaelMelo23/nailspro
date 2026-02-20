package com.rafael.nailspro.webapp.infrastructure.whatsapp.evolution;

import com.rafael.nailspro.webapp.application.whatsapp.WhatsappProvider;
import com.rafael.nailspro.webapp.domain.enums.EvolutionEvent;
import com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.CreateInstanceRequestDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.SendTextRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "whatsapp.provider", havingValue = "evolution", matchIfMissing = true)
public class EvolutionWhatsappService implements WhatsappProvider {

    private final RestTemplate restTemplate;

    @Value("${evolution.url}")
    private String evolutionApiBaseUrl;

    @Value("${evolution.apikey}")
    private String evolutionApiKey;

    @Value("${evolution.webhook.url}")
    private String webhookUrl;

    public String createInstance(String tenantId) {
        String url = evolutionApiBaseUrl + "/instance/create/";
        CreateInstanceRequestDTO dto = createInstanceRequestPayload(tenantId);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, dto, String.class);
            return response.getBody();
        } catch (HttpClientErrorException.Conflict e) {
            return null;
        }
    }

    public CreateInstanceRequestDTO createInstanceRequestPayload(String tenantId) {
        List<EvolutionEvent> eventosDesejados = List.of(
                EvolutionEvent.QRCODE_UPDATED,
                EvolutionEvent.SEND_MESSAGE,
                EvolutionEvent.CONNECTION_UPDATE
        );

        return CreateInstanceRequestDTO.builder()
                .token(evolutionApiKey)
                .events(eventosDesejados)
                .instanceName(tenantId)
                .webhookByEvents(false)
                .webhook(webhookUrl)
                .number(null)
                .qrcode(true).build();
    }

    public String instanceConnect(String instanceName, Optional<String> phoneNumber) {
        String url = evolutionApiBaseUrl + "/instance/connect/" + instanceName;

        if (phoneNumber.isPresent()) {
            url = url + "?phoneNumber=" + phoneNumber.get();
        }

        HttpEntity<Void> request = new HttpEntity<>(getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        return response.getBody();
    }

    public void sendTextVoid(String instanceName, String message, String targetNumber) {
        String url = evolutionApiBaseUrl + "/message/sendText/" + instanceName;

        SendTextRequestDTO body = SendTextRequestDTO.of(
                formatPhoneNumber(targetNumber),
                message
        );

        HttpEntity<SendTextRequestDTO> request = new HttpEntity<>(body, getHeaders());
        restTemplate.postForEntity(url, request, String.class);
    }

    public Map<String, Object> sendText(String instanceName, String message, String targetNumber) {
        String url = evolutionApiBaseUrl + "/message/sendText/" + instanceName;

        SendTextRequestDTO bodyDTO = SendTextRequestDTO.of(
                formatPhoneNumber(targetNumber),
                message
        );

        HttpEntity<SendTextRequestDTO> requestBody = new HttpEntity<>(bodyDTO, getHeaders());
        return restTemplate.postForObject(url, requestBody, Map.class);
    }

    public void logout(String instanceName) {
        String url = evolutionApiBaseUrl + "/instance/logout/" + instanceName;

        HttpEntity<Void> request = new HttpEntity<>(getHeaders());
        restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("apikey", evolutionApiKey);
        httpHeaders.set("Content-Type", "application/json");
        return httpHeaders;
    }

    private String formatPhoneNumber(String phoneNumber) {
        String cleanNumber = phoneNumber.replaceAll("\\D", "");
        return cleanNumber.startsWith("55") ? cleanNumber : "55" + cleanNumber;
    }
}