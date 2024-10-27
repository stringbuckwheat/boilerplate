package com.memil.setting.auth.service;

import com.memil.setting.auth.dto.LoginRequest;
import com.memil.setting.auth.dto.LoginResponse;
import com.memil.setting.auth.token.AccessToken;
import com.memil.setting.auth.token.RefreshToken;
import com.memil.setting.common.error.ErrorMessage;
import com.memil.setting.common.error.exception.RefreshTokenException;
import com.memil.setting.user.model.User;
import com.memil.setting.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final TokenService tokenService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.WRONG_USERNAME.getMessage()));

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(ErrorMessage.WRONG_PASSWORD.getMessage());
        }

        return authenticateAndGenerateTokens(user);
    }

    public LoginResponse authenticateAndGenerateTokens(User user) {
        // Security Context 저장
        tokenService.setAuthentication(user.getId(), user.getUsername());

        // Access Token, Refresh Token 생성
        AccessToken accessToken = tokenService.generateAccessToken(user.getId(), user.getUsername());
        RefreshToken refreshToken = tokenService.generateRefreshToken(user.getId(), user.getUsername());

        return new LoginResponse(user, accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public String reissueAccessToken(String refreshToken) {
        return tokenService.reissueAccessToken(refreshToken).getToken();
    }

    @Transactional
    public void removeAuthentication(String refreshToken) {
        if(!StringUtils.hasText(refreshToken)) {
            throw new RefreshTokenException(ErrorMessage.NO_REFRESH_TOKEN.getMessage());
        }

        tokenService.deleteRefreshToken(refreshToken);
    }
}

