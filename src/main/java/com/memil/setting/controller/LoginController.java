package com.memil.setting.controller;

import com.memil.setting.AccessToken;
import com.memil.setting.auth.SecretKey;
import com.memil.setting.entity.User;
import com.memil.setting.oauth2.UserPrincipal;
import com.memil.setting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SecretKey secretKey;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        User memil = userRepository.findByUsername(request.get("username")).orElseThrow(() -> new UsernameNotFoundException("WRONG USERNAME"));

        // 비밀번호 일치 여부 확인
        if(!passwordEncoder.matches(request.get("password"), memil.getPassword())){
            throw new BadCredentialsException("WRONG PASSWORD");
        }

        // Security Context 저장
        UserPrincipal userPrincipal = UserPrincipal.create(memil);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userPrincipal, request.get("password"), Collections.singleton(new SimpleGrantedAuthority("USER")));
        SecurityContextHolder.getContext().setAuthentication(token);

        // response 만들기
        String accessToken = new AccessToken(userPrincipal, secretKey.getKey()).getToken();

        Map<String, String> response = new HashMap<>();

        response.put("username", memil.getUsername());
        response.put("accessToken", accessToken); // AccessToken 추가
        response.put("message", "로그인 성공");


        return response;
    }

    // /auth/** 모양의 주소는 모두 인증 필요
    @GetMapping("/auth/test")
    public Map<String, Object> authTest(@AuthenticationPrincipal UserPrincipal user){
        log.debug("user: {}", user);

        Map<String, Object> map = new HashMap<>();
        map.put("authTest", user);

        return map;
    }
}
