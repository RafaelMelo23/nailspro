package com.rafael.nailspro.webapp.controller.api.auth;

import com.rafael.nailspro.webapp.model.dto.auth.LoginDTO;
import com.rafael.nailspro.webapp.model.dto.auth.RegisterDTO;
import com.rafael.nailspro.webapp.service.auth.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO,
                                      HttpServletResponse response) {

        String token = authenticationService.login(loginDTO);

        Cookie cookie = new Cookie("AUTH_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);

        response.addCookie(cookie);
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDTO registerDTO) {

        authenticationService.register(registerDTO);
        return ResponseEntity.ok().build();
    }
}
