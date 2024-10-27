package com.memil.setting.auth.model;

import com.memil.setting.auth.token.RefreshToken;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Getter
@RedisHash(value = "activeUser", timeToLive = 14400) // 4시간 저장
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActiveUser {
    @Id
    private String refreshToken;

    private LocalDateTime expiredAt; // 만료 시각

    private Long id; // user pk

    private String username;

    private LocalDateTime createdAt;

    public ActiveUser(Long id, String username, RefreshToken refreshToken) {
        this.id = id;
        this.username = username;

        this.refreshToken = refreshToken.getToken();
        this.expiredAt = refreshToken.getExpiredAt();

        this.createdAt = LocalDateTime.now();
    }
}