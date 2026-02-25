package com.rafael.nailspro.webapp.infrastructure.controller.api.auth;

import com.rafael.nailspro.webapp.application.auth.AuthenticationService;
import com.rafael.nailspro.webapp.infrastructure.dto.auth.AuthResultDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.auth.LoginDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.auth.RegisterDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.auth.TokenRefreshResponseDTO;
import jakarta.validation.Valid;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO,
                                      HttpServletResponse response) {

        AuthResultDTO authResultDTO = authenticationService.login(loginDTO);

        addRefreshTokenCookie(response, authResultDTO.refreshToken());

        return ResponseEntity.ok().body(authResultDTO.jwtToken());
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterDTO registerDTO) {

        authenticationService.register(registerDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@CookieValue(name = "refresh_token") String refreshToken,
                                        HttpServletResponse response) {

        TokenRefreshResponseDTO tokenRefreshResponseDTO = authenticationService.refreshToken(refreshToken);

        addRefreshTokenCookie(response, tokenRefreshResponseDTO.refreshToken());

        return ResponseEntity.ok(tokenRefreshResponseDTO.jwtToken());
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("refresh_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/v1/auth/refresh");
        cookie.setMaxAge(60 * 60 * 24 * 7);
        response.addCookie(cookie);
    }
}