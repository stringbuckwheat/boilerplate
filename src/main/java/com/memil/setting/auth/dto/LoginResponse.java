package com.memil.setting.auth.dto;

import com.memil.setting.auth.token.AccessToken;
import com.memil.setting.auth.token.RefreshToken;
import com.memil.setting.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 로그인 성공 시 리턴할 DTO
 */
@Getter
@ToString
public class LoginResponse {
    private String username;
    private String name;
    private String accessToken;
    private String refreshToken;

    public LoginResponse(User user, AccessToken accessToken, RefreshToken refreshToken) {
        this.username = user.getUsername();
        this.name = user.getName();
        this.accessToken = accessToken.getToken();
        this.refreshToken = refreshToken.getToken();
    }
}

