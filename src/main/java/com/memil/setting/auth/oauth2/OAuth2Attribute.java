package com.memil.setting.auth.oauth2;

import com.memil.setting.common.enums.Provider;
import com.memil.setting.user.model.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {
    private Map<String, Object> attributes;
    private String email;
    private String name;
    private String picture;
    private Provider provider;

    public static OAuth2Attribute of(String provider, Map<String, Object> attributes) {
        return switch (provider) {
            case "google" -> ofGoogle(attributes);
            case "kakao" -> ofKakao(attributes);
            default -> throw new RuntimeException();
        };
    }

    private static OAuth2Attribute ofGoogle(Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .name(String.valueOf(attributes.get("name")))
                .email(String.valueOf(attributes.get("email")))
                .picture(String.valueOf(attributes.get("picture")))
                .attributes(attributes)
                .provider(Provider.GOOGLE)
                .build();
    }

    private static OAuth2Attribute ofKakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attribute.builder()
                .name(String.valueOf(kakaoProfile.get("nickname")))
                .email(String.valueOf(kakaoAccount.get("email")))
                .picture(String.valueOf(kakaoProfile.get("profile_image_url")))
                .attributes(kakaoAccount)
                .provider(Provider.KAKAO)
                .build();
    }

    public User toEntity() {
        // 신규 가입 시 Attribute를 바탕으로 회원 엔티티 만들기
        return User.builder()
                .email(this.getEmail())
                .name(this.getName())
                .provider(this.provider)
                .build();
    }
}
