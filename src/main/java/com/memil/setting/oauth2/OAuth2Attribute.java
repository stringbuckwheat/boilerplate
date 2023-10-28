package com.memil.setting.oauth2;

import com.memil.setting.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

// MEMIL OAuth2 성공 정보를 담는 클래스
// 이 보일러 플레이트에선 email, name 정도만 필요하지만,
// 실제적으론 다른 정보가 필요할 경우가 많을 것 같아 여러 정보를 포함합니다
@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {
    private Map<String, Object> attributes;
    private String email;
    private String name;
    private String picture;
    private String provider;

    public static OAuth2Attribute of(String provider, Map<String, Object> attributes) {
        // MEMIL 다른 서비스를 추가할 예정이라면 이곳에 추가한 뒤, of 메소드를 구현하세요
        switch (provider) {
            case "google":
                return ofGoogle(attributes);
            case "kakao":
                return ofKakao(attributes);
            default:
                throw new RuntimeException();
        }
    }

    private static OAuth2Attribute ofGoogle(Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .name(String.valueOf(attributes.get("name")))
                .email(String.valueOf(attributes.get("email")))
                .picture(String.valueOf(attributes.get("picture")))
                .attributes(attributes)
                .provider("google")
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
                .provider("kakao")
                .build();
    }

    public User toEntity() {
        // 신규 가입 시 Attribute를 바탕으로 회원 엔티티 만들기
        return User.builder()
                .username(this.getEmail())
                .password(null)
                .name(this.getName())
                .provider(this.provider)
                .build();
    }
}
