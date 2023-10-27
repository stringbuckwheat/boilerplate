package com.memil.setting.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// MEMIL 인증 관련 Exception 처리를 위한 필터입니다
// 필터를 두 개 만들기 싫다면, JwtFilter의 doFilterInternal 코드를 try-catch로 감아줘도 됩니다.
@Component
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response); // go to 'JwtAuthenticationFilter'
        } catch (ExpiredJwtException e) {
            setErrorResponse(response, "ACCESS_TOKEN_EXPIRED");
        } catch (MalformedJwtException e) {
            log.debug("JWT EXCEPTION FILTER");
            setErrorResponse(response, "INVALID JWT TOKEN");
        } catch (JwtException | SecurityException e){
            setErrorResponse(response, "CANNOT LOGIN");
        }
    }

    public void setErrorResponse(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json; charset=UTF-8");

        Map<String, String> map = new HashMap<>();
        map.put("errorCode", "B001"); // 프론트에서 로그아웃 유도로 사용할 코드입니다.
        map.put("msg", msg);

        response.getWriter().write(objectMapper.writeValueAsString(map));
    }
}
