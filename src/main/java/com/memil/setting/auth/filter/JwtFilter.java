package com.memil.setting.auth.filter;

import com.memil.setting.auth.dto.UserPrincipal;
import com.memil.setting.auth.service.TokenService;
import com.memil.setting.auth.token.AccessToken;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 로그인 이후 access token 검증하는 필터
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final TokenService tokenService;

    private String resolveToken(HttpServletRequest request) {
        // Request header에서 토큰 가져오기
        String token = request.getHeader(AUTHORIZATION_HEADER);

        // 공백 혹은 null이 아니고 Bearer로 시작하면
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            // Bearer 떼고 줌
            return token.split(" ")[1].trim();
        }

        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("*** JWT FILTER *** 주소: {}", request.getRequestURL());

        String token = resolveToken(request);

        // 디코딩할만한 토큰이 왔으면
        if (token != null) {
            // header의 token로 token, key를 포함하는 새로운 JwtAuthToken 만들기
            AccessToken accessToken = tokenService.convertAccessToken(token);
            Claims claims = accessToken.getData();

            // SecurityContext 설정
            Long id = Long.parseLong(claims.getSubject());
            String username = String.valueOf(claims.get("aud"));

            tokenService.setAuthentication(id, username);
        }

        filterChain.doFilter(request, response);
    }
}
