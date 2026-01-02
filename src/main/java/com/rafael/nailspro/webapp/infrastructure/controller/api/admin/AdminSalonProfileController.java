package com.rafael.nailspro.webapp.infrastructure.controller.api.admin;

import com.rafael.nailspro.webapp.infrastructure.dto.admin.salon.profile.SalonProfileDTO;
import com.rafael.nailspro.webapp.domain.user.UserPrincipal;
import com.rafael.nailspro.webapp.domain.admin.salon.profile.AdminSalonProfileService;
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
public class AdminSalonProfileController {

    private final AdminSalonProfileService adminSalonProfileService;

    @PostMapping
    public ResponseEntity<Void> createOrUpdateProfile(@AuthenticationPrincipal UserPrincipal user,
                                                      @RequestBody SalonProfileDTO dto) throws IOException {

        adminSalonProfileService.createOrUpdateProfile(user.getUserId(), dto);
        return ResponseEntity.ok().build();
    }
}
