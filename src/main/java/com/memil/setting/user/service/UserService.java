package com.memil.setting.user.service;

import com.memil.setting.auth.dto.LoginResponse;
import com.memil.setting.auth.service.AuthService;
import com.memil.setting.common.error.ErrorMessage;
import com.memil.setting.common.error.exception.HasSameUsernameException;
import com.memil.setting.user.dto.RegisterRequest;
import com.memil.setting.user.dto.UserResponse;
import com.memil.setting.user.dto.UserUpdate;
import com.memil.setting.user.model.User;
import com.memil.setting.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthService authService;

    // 아이디 중복 검사
    @Transactional(readOnly = true)
    public String hasSameUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            throw new HasSameUsernameException(username + "은/는 이미 사용중인 아이디입니다.");
        }

        return username;
    }

    // 회원 가입
    @Transactional
    public LoginResponse register(RegisterRequest registerRequest) {
        // 아이디 중복 검사
        hasSameUsername(registerRequest.getUsername());

        // 비밀번호 암호화
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // 저장
        User user = userRepository.save(registerRequest.toEntity());

        // 로그인 처리
        return authService.authenticateAndGenerateTokens(user);
    }

    // 회원 정보 보기
    @Transactional(readOnly = true)
    public UserResponse getDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        return new UserResponse(user);
    }

    // 회원 정보 변경 - name, email
    @Transactional
    public UserResponse update(UserUpdate userUpdate, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        user.update(userUpdate.getName(), userUpdate.getEmail());
        return new UserResponse(user);
    }

    // 비밀번호 수정
    public void updatePassword(String password, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        user.updatePassword(passwordEncoder.encode(password));
    }

    // 회원 탈퇴
    public void delete(Long userId, String refreshToken) {
        // Redis 삭제
        authService.removeAuthentication(refreshToken);

        // 회원 삭제
        userRepository.deleteById(userId);
    }
}
