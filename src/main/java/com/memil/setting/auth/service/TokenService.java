package com.memil.setting.auth.service;

import com.memil.setting.auth.dto.UserPrincipal;
import com.memil.setting.auth.model.ActiveUser;
import com.memil.setting.auth.repository.ActiveUserRepository;
import com.memil.setting.auth.token.AccessToken;
import com.memil.setting.auth.token.RefreshToken;
import com.memil.setting.common.error.ErrorMessage;
import com.memil.setting.common.error.exception.RefreshTokenException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {
    private final ActiveUserRepository activeUserRepository;
    @Value("${secret}")
    private String secret;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    public AccessToken convertAccessToken(String token) {
        return new AccessToken(token, key);
    }

    public void setAuthentication(Long userId, String username) {
        UserPrincipal userCustom = new UserPrincipal(userId, username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userCustom, null, Collections.singleton(new SimpleGrantedAuthority("USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    public AccessToken generateAccessToken(Long userId, String username) {
        return new AccessToken(userId, username, key);
    }

    public RefreshToken generateRefreshToken(Long userId, String username) {
        RefreshToken refreshToken = new RefreshToken();

        // Refresh token Redis에 저장
        ActiveUser activeUser = new ActiveUser(userId, username, refreshToken);
        activeUserRepository.save(activeUser);

        return refreshToken;
    }

    public AccessToken reissueAccessToken(String refreshToken) {
        ActiveUser activeUser = activeUserRepository.findById(refreshToken)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.REFRESH_TOKEN_NOT_FOUND.getMessage()));

        if (activeUser.getExpiredAt().isBefore(LocalDateTime.now())) {
            // refresh token 만료
            activeUserRepository.deleteById(refreshToken); // Redis에서 삭제
            throw new RefreshTokenException(ErrorMessage.REFRESH_TOKEN_EXPIRED.getMessage());
        }

        return generateAccessToken(activeUser.getId(), activeUser.getUsername());
    }

    public void deleteRefreshToken(String refreshToken) {
        activeUserRepository.deleteById(refreshToken);
    }
}