package com.memil.setting.auth.oauth2;

import com.memil.setting.auth.dto.UserPrincipal;
import com.memil.setting.auth.service.TokenService;
import com.memil.setting.auth.token.AccessToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final String TARGET_URL = "http://localhost:3000/oauth/";

    // 로그인 성공 시 부가작업
    // JWT 발급 후 token과 함께 리다이렉트
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

        // access 토큰 생성
        String accessToken = tokenService.generateAccessToken(user.getId(), user.getUsername()).getToken();
        getRedirectStrategy().sendRedirect(request, response, TARGET_URL + accessToken);
    }
}
