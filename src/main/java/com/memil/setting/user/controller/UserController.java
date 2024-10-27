package com.memil.setting.user.controller;

import com.memil.setting.auth.dto.LoginResponse;
import com.memil.setting.auth.dto.UserPrincipal;
import com.memil.setting.user.dto.RegisterRequest;
import com.memil.setting.user.dto.UpdatePassword;
import com.memil.setting.user.dto.UserResponse;
import com.memil.setting.user.dto.UserUpdate;
import com.memil.setting.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/api/user/username")
    public ResponseEntity<String> hasSameUsername(@RequestBody String username) {
        return ResponseEntity.ok().body(userService.hasSameUsername(username));
    }

    @PostMapping("/api/user")
    public ResponseEntity<LoginResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request));
    }

    @PutMapping("/api/user/{id}")
    public ResponseEntity<UserResponse> update(@RequestBody @Valid UserUpdate userUpdate, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok().body(userService.update(userUpdate, userPrincipal.getId()));
    }

    @PutMapping("/api/user/{id}/password")
    public ResponseEntity<Void> updatePassword(@RequestBody @Valid UpdatePassword updatePassword,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        userService.updatePassword(updatePassword.getPassword(), userPrincipal.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/user/{id}")
    public ResponseEntity<Void> delete(@RequestBody String refreshToken, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        userService.delete(userPrincipal.getId(), refreshToken);
        return ResponseEntity.noContent().build();
    }
}
