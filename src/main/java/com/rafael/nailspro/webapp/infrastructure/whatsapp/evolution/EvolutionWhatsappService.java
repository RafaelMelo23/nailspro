package com.rafael.nailspro.webapp.infrastructure.whatsapp.evolution;

import com.rafael.nailspro.webapp.domain.enums.EvolutionEvent;
import com.rafael.nailspro.webapp.application.whatsapp.WhatsappProvider;
import com.rafael.nailspro.webapp.domain.tenant.TenantInstance;
import com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.CreateInstanceRequestDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.whatsapp.evolution.SendTextRequestDTO;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "whatsapp.provider", havingValue = "evolution", matchIfMissing = true)
public class EvolutionWhatsappService implements WhatsappProvider {

    /*
        - configurar webhook pro evolution nos informar dos envios das mensagens
        - configurar scheduled pra rodar e enviar as mensagens
        - configurar parte da aplicacao para que o tenant logue com seu whatsapp no evolution

        - criar as classes/controller relacionadas ao webhook:
            - service que processa cada EvolutionEvent
            - enviar via SSE caso desconecte

         - criar a classe p criacao da instancia (criar atributo na SalonProfile: boolean hasInstance
     */

    private final RestTemplate restTemplate;

    @Value("${evolution.url:placeholder}")
    private String evolutionApiBaseUrl;

    @Value("${evolution.apikey:placeholder}")
    private String globalApiKey;

    @Value("${evolution.webhook.url:placeholder}")
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
                EvolutionEvent.MESSAGES_UPDATE,
                EvolutionEvent.SEND_MESSAGE,
                EvolutionEvent.CONNECTION_UPDATE
        );

        return CreateInstanceRequestDTO.builder()
                .token(globalApiKey)
                .events(eventosDesejados)
                .instanceName(TenantInstance.format(tenantId))
                .webhookByEvents(false)
                .webhook(webhookUrl)
                .number(null)
                .qrcode(true).build();
    }

    public String connectInstanceViaPhoneNumber(String instanceName, String phoneNumber) {
        String url = evolutionApiBaseUrl + "/instance/registerMobileCode/" + instanceName;

        String mobileCode = formatPhoneNumber(phoneNumber);
        HttpEntity<String> request = new HttpEntity<>(mobileCode, getHeaders());

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return response.getBody();
    }

    public String connectInstanceViaQrCode(String instanceName, String phoneNumber) {

        String url = evolutionApiBaseUrl + "/instance/connect/" + instanceName + "?number=" + phoneNumber;

        HttpEntity<Void> request = new HttpEntity<>(getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        return response.getBody();
    }

    public void sendText(String instanceName, String message, String targetNumber) {

        String url = evolutionApiBaseUrl + "/message/sendText/" + instanceName + "/" + targetNumber;

        SendTextRequestDTO body = new SendTextRequestDTO(formatPhoneNumber(targetNumber), message);

        HttpEntity<SendTextRequestDTO> request = new HttpEntity<>(body, getHeaders());

        restTemplate.postForEntity(url, request, String.class);
    }

    public void logout(String instanceName) {

        String url = evolutionApiBaseUrl + "/instance/logout/" + instanceName;

        HttpEntity<Void> request = new HttpEntity<>(getHeaders());
        restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
    }

    private HttpHeaders getHeaders() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("apiKey", globalApiKey);
        httpHeaders.set("Content-Type", "application/json");
        return httpHeaders;
    }

    private String formatPhoneNumber(String phoneNumber) {

        String cleanNumber = phoneNumber.replaceAll("\\D", "");

        if (!cleanNumber.startsWith("55")) {
            return "55" + cleanNumber;
        }

        return cleanNumber;
    }
}
