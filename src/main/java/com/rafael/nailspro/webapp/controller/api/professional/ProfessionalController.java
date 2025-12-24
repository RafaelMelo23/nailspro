package com.rafael.nailspro.webapp.controller.api.professional;

import com.rafael.nailspro.webapp.model.dto.professional.ProfessionalSimplifiedDTO;
import com.rafael.nailspro.webapp.service.professional.ProfessionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/professional")
public class ProfessionalController {

    private final ProfessionalService professionalService;

    @GetMapping("/simplified")
    public ResponseEntity<List<ProfessionalSimplifiedDTO>> getProfessionals() {

        return ResponseEntity.ok(professionalService.findAllSimplified());
    }
}
