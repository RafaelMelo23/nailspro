package com.rafael.nailspro.webapp.controller.api.auth;

import com.rafael.nailspro.webapp.model.dto.auth.ChangeEmailRequestDTO;
import com.rafael.nailspro.webapp.model.dto.auth.ChangePhoneRequestDTO;
import com.rafael.nailspro.webapp.model.dto.auth.ResetPasswordDTO;
import com.rafael.nailspro.webapp.model.entity.user.UserPrincipal;
import com.rafael.nailspro.webapp.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PatchMapping("/email")
    public ResponseEntity<Void> updateEmail(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @RequestBody ChangeEmailRequestDTO dto) {

        userService.updateEmail(userPrincipal.getId(), dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/phone")
    public ResponseEntity<Void> updatePhone(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @RequestBody ChangePhoneRequestDTO dto) {

        userService.updatePhone(userPrincipal.getId(), dto);
        return ResponseEntity.noContent().build();
    }

    //todo: test
    @PostMapping("/password/forgot")
    public ResponseEntity<Void> forgotPassword(String userEmail) {

        userService.forgotPasswordRequest(userEmail);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/password/update")
    public ResponseEntity<Void> updatePassword(@RequestBody ResetPasswordDTO dto) {

        userService.resetPassword(dto);
        return ResponseEntity.noContent().build();
    }
}
