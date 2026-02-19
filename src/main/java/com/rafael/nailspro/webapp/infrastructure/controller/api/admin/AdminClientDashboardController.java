package com.rafael.nailspro.webapp.infrastructure.controller.api.admin;

import com.rafael.nailspro.webapp.application.admin.dashboard.ClientDashboardService;
import com.rafael.nailspro.webapp.infrastructure.dto.client.AdminClientCrmDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/v1/admin/dashboard/clients")
public class AdminClientDashboardController {

    private final ClientDashboardService service;

    @GetMapping("/{clientId}")
    public ResponseEntity<AdminClientCrmDTO> getClientDashboardInfo(
            @PathVariable Long clientId) {

        AdminClientCrmDTO response = service.getClientsAuditedInfo(clientId);

        return ResponseEntity.ok(response);
    }
}