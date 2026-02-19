package com.rafael.nailspro.webapp.infrastructure.controller.api.admin;

import com.rafael.nailspro.webapp.application.admin.salon.profile.SalonProfileManagementService;
import com.rafael.nailspro.webapp.domain.model.UserPrincipal;
import com.rafael.nailspro.webapp.infrastructure.dto.admin.salon.profile.SalonProfileDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/salon/profile")
public class SalonProfileController {

    private final SalonProfileManagementService salonProfileManagementService;

    @PostMapping
    public ResponseEntity<Void> createOrUpdateProfile(@AuthenticationPrincipal UserPrincipal user,
                                                      @RequestBody SalonProfileDTO dto) throws IOException {

        salonProfileManagementService.createOrUpdateProfile(user.getUserId(), dto);
        return ResponseEntity.ok().build();
    }
}
