package com.memil.setting.user.model;

import com.memil.setting.common.enums.Provider;
import com.memil.setting.common.model.TimeStamped;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; // 자체 로그인은 아이디, 소셜 로그인은 이메일 저장

    private String password;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private Provider provider; // local, google, kakao...

    @Builder
    public User(String username, String password, String name, String email, Provider provider) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.provider = provider;
    }

    public void update(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
