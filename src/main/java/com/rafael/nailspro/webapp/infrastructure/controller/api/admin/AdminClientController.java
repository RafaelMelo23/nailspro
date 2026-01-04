package com.rafael.nailspro.webapp.infrastructure.controller.api.admin;

import com.rafael.nailspro.webapp.infrastructure.dto.admin.client.ClientAppointmentDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.admin.client.ClientDTO;
import com.rafael.nailspro.webapp.domain.enums.UserStatus;
import com.rafael.nailspro.webapp.application.admin.client.AdminClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/client")
public class AdminClientController {

    private final AdminClientService adminClientService;

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/status/{userId}/{userStatus}")
    public ResponseEntity<Void> updateClientStatus(
            @PathVariable Long userId,
            @PathVariable UserStatus userStatus) {

        adminClientService.changeClientStatus(userId, userStatus);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<ClientDTO>> searchClients(
            @RequestParam(required = false, defaultValue = "") String name,
            Pageable pageable) {

        Page<ClientDTO> clients =
                adminClientService.searchForClients(name, pageable);

        return ResponseEntity.ok(clients);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{clientId}/appointments")
    public ResponseEntity<Page<ClientAppointmentDTO>> getClientAppointments(
            @PathVariable Long clientId,
            Pageable pageable) {

        Page<ClientAppointmentDTO> appointments =
                adminClientService.getClientAppointments(clientId, pageable);

        return ResponseEntity.ok(appointments);
    }


}
