package com.memil.setting.auth.token;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class RefreshToken {
    private String token;
    private LocalDateTime expiredAt;

    public RefreshToken() {
        this.token = UUID.randomUUID().toString();
        this.expiredAt = LocalDateTime.now().plusHours(3);
    }

    public RefreshToken(String token, LocalDateTime expiredAt) {
        this.token = token;
        this.expiredAt = expiredAt;
    }
}
