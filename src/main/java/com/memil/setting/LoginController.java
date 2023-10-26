package com.memil.setting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class LoginController {

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        // MEMIL DB에서 받아왔다고 가정
        Map<String, String> mockUser = new HashMap<>();
        mockUser.put("username", "memil");
        mockUser.put("password", "loveyoumemil");

        if (!request.get("username").equals(mockUser.get("username")) || !request.get("password").equals(mockUser.get("password"))) {
            throw new BadCredentialsException("로그인 정보를 다시 확인해주세요");
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "로그인 성공");

        return response;
    }
}
