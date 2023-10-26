package com.memil.setting.controller;

import com.memil.setting.entity.User;
import com.memil.setting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        // JPA 쿼리 메소드
        // User memil = userRepository.findById(request.get("username")).orElseThrow(() -> new UsernameNotFoundException("해당 아이디 없음"));

        // QueryDsl 메소드
        User memil = userRepository.findByUsername(request.get("username"));

        // 비밀번호 일치 여부 확인
        if(!passwordEncoder.matches(request.get("password"), memil.getPassword())){
            throw new BadCredentialsException("로그인 정보를 다시 확인해주세요");
        }

        // response 만들기
        Map<String, String> response = new HashMap<>();
        response.put("username", memil.getUsername());
        response.put("message", "로그인 성공");

        return response;
    }
}
