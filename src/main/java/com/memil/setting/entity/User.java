package com.memil.setting.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@ToString(of = {"username"})
@Getter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username; // MEMIL 자체 로그인은 아이디, 소셜 로그인은 이메일이 저장됩니다.
    private String password;
    private String name;
    private String provider; // local, google, kakao...
}
