package com.memil.setting.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memil.setting.common.error.ErrorMessage;
import com.memil.setting.common.error.ErrorResponse;
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

@Component
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // JWT 필터에서 발생한 예외처리
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            // Access Token 만료
            setErrorResponse(response, ErrorMessage.ACCESS_TOKEN_EXPIRED.getMessage());
        } catch (MalformedJwtException e) {
            setErrorResponse(response, ErrorMessage.INVALID_ACCESS_TOKEN.getMessage());
        } catch (JwtException | SecurityException e){
            setErrorResponse(response, ErrorMessage.UNEXPECTED_ERROR_OCCUR.getMessage());
        }
    }

    private void setErrorResponse(HttpServletResponse response, String message) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(message);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json; charset=UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
