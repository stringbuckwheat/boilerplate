package com.memil.setting;

import com.memil.setting.entity.User;
import com.memil.setting.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.NoSuchElementException;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("유저 데이터 DB INSERT")
    void insertUsers(){
        User memil = userRepository.save(new User("memil", passwordEncoder.encode("loveyoumemil")));
        User dani = userRepository.save(new User("dani", passwordEncoder.encode("loveyoudani")));
    }

    @Test
    @DisplayName("JPA 쿼리 메소드 / QueryDsl 메소드 테스트")
    void queryDslTest(){
        User memil = userRepository.findById("memil").orElseThrow(NoSuchElementException::new);
        User memil2 = userRepository.findByUsername("memil");

        Assertions.assertThat(memil.getUsername()).isEqualTo(memil2.getUsername());
    }
}
