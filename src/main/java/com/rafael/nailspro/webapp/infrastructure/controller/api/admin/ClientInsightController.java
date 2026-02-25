package com.rafael.nailspro.webapp.infrastructure.controller.api.admin;

import com.rafael.nailspro.webapp.application.admin.dashboard.ClientInsightService;
import com.rafael.nailspro.webapp.infrastructure.dto.client.AdminClientCrmDTO;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/v1/admin/insight/clients")
public class ClientInsightController {

    private final ClientInsightService service;

    @GetMapping("/{clientId}")
    public ResponseEntity<AdminClientCrmDTO> getClientDashboardInfo(
            @PathVariable @Positive(message = "O identificador do cliente deve ser positivo") Long clientId) {

        AdminClientCrmDTO response = service.getClientsAuditedInfo(clientId);

        return ResponseEntity.ok(response);
    }
}