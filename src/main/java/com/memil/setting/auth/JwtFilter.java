package com.memil.setting.auth;

import com.memil.setting.AccessToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

// 로그인 이후 access token 검증하는 필터
@Slf4j
@AllArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final SecretKey secretKey;

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
        // if 블록 잘못 없으면 permitAll 주소에서 오류나요 ㅋㅋㅋ
        if (token != null) {
            // header의 token로 token, key를 포함하는 새로운 JwtAuthToken 만들기
            AccessToken accessToken = new AccessToken(token, secretKey.getKey());
            Claims claims = accessToken.getData();

            // 유저 객체 생성
            // MEMIL 간단한 코드라 사용자 정보가 username밖에 없습니다.
            // 추가적인 사용자 정보가 필요하다면 UserPrincipal 객체를 커스터마이징하여
            // 첫번째 인자로 넘겨주면 됩니다
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    claims.get("aud"),
                    null,
                    Collections.singleton(new SimpleGrantedAuthority("USER")));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug("username: {}", claims.get("aud"), " 인증 성공!");
        }

        filterChain.doFilter(request, response);
    }
}
