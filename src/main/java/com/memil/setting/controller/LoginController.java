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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        User memil = userRepository.findById(request.get("username")).orElseThrow(() -> new UsernameNotFoundException("해당 아이디 없음"));

        // 비밀번호 일치 여부 확인
        if(!passwordEncoder.matches(request.get("password"), memil.getPassword())){
            throw new BadCredentialsException("로그인 정보를 다시 확인해주세요");
        }

        // Security Context 저장
        UserPrincipal userPrincipal = new UserPrincipal(memil.getUserId(), memil.getName());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userPrincipal, request.get("password"), Collections.singleton(new SimpleGrantedAuthority("USER")));
        SecurityContextHolder.getContext().setAuthentication(token);

        // response 만들기
        Map<String, String> response = new HashMap<>();
        response.put("username", memil.getUsername());
        response.put("accessToken", new AccessToken(userPrincipal, secretKey.getKey()).getToken()); // AccessToken 추가
        response.put("message", "로그인 성공");

        return response;
    }

    // /auth/** 모양의 주소는 모두 인증 필요
    @GetMapping("/auth/test")
    public Map<String, String> authTest(){
        Map<String, String> map = new HashMap<>();
        map.put("authTest", "성공");

        return map;
    }
}
