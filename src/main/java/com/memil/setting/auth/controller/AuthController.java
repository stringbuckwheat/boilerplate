package com.memil.setting.auth.controller;

import com.memil.setting.auth.dto.LoginRequest;
import com.memil.setting.auth.dto.LoginResponse;
import com.memil.setting.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/api/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok().body(authService.login(loginRequest));
    }

    @PostMapping("/api/token/reissue")
    public ResponseEntity<String> reissueAccessToken(@RequestBody String refreshToken) {
        return ResponseEntity.ok().body(authService.reissueAccessToken(refreshToken));
    }

    @PostMapping("/api/logout")
    public ResponseEntity<Void> logOut(@RequestBody String refreshToken) {
        authService.removeAuthentication(refreshToken);
        return ResponseEntity.noContent().build();
    }
}
