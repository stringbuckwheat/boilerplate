package com.memil.setting.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// 예외처리
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UsernameNotFoundException.class})
    public HttpEntity<Map<String, String>> handleUsernameNotFoundException(Exception e){
        Map<String, String> map = new HashMap<>();
        map.put("errorCode", "B002"); // 아이디 틀림
        map.put("msg", e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(map);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public HttpEntity<Map<String, String>> handleBadCredentialsException(Exception e){
        Map<String, String> map = new HashMap<>();
        map.put("errorCode", "B002"); // 비번 틀림
        map.put("msg", e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(map);
    }
}
