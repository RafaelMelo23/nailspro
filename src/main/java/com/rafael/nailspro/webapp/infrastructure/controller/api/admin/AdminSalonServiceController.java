package com.rafael.nailspro.webapp.infrastructure.controller.api.admin;

import com.rafael.nailspro.webapp.application.salon.service.SalonServiceService;
import com.rafael.nailspro.webapp.infrastructure.dto.salon.service.SalonServiceDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.salon.service.SalonServiceOutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/salon/service")
public class AdminSalonServiceController {

    private final SalonServiceService salonService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createSalonService(@RequestBody SalonServiceDTO salonServiceDTO) {

        salonService.createService(salonServiceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<SalonServiceOutDTO>> getAllSalonServices() {

        return ResponseEntity.ok(salonService.getServices());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/active/{serviceId}/{isActive}")
    public ResponseEntity<Void> changeSalonServiceVisibility(@PathVariable Long serviceId,
                                                             @PathVariable Boolean isActive) {

        salonService.changeSalonServiceVisibility(serviceId, isActive);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/active/{serviceId}")
    public ResponseEntity<Void> deleteSalonService(@PathVariable Long serviceId) {

        salonService.deleteSalonService(serviceId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{serviceId}")
    public ResponseEntity<Void> updateSalonService(@RequestBody SalonServiceDTO salonServiceDTO,
                                                   @PathVariable Long serviceId) {

        salonService.updateSalonService(serviceId, salonServiceDTO);
        return ResponseEntity.noContent().build();
    }
}
