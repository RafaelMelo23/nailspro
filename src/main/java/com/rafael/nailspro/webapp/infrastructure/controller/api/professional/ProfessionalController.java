package com.rafael.nailspro.webapp.infrastructure.controller.api.professional;

import com.rafael.nailspro.webapp.application.client.ClientAppointmentUseCase;
import com.rafael.nailspro.webapp.application.professional.ProfessionalQueryService;
import com.rafael.nailspro.webapp.domain.model.UserPrincipal;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.ProfessionalAvailabilityDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.professional.FindProfessionalAvailabilityDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.professional.ProfessionalSimplifiedDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/professional")
public class ProfessionalController {

    private final ProfessionalQueryService professionalQueryService;

    @GetMapping("/simplified")
    public ResponseEntity<List<ProfessionalSimplifiedDTO>> getProfessionals() {

        return ResponseEntity.ok(professionalQueryService.findAllSimplified());
    }
}
