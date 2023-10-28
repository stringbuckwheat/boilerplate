package com.memil.setting.oauth2;

import com.memil.setting.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

// MEMIL Authenticaion용 객체
// 기본 로그인, 소셜 로그인 모두 같은 객체를 쓰고 싶어 커스터마이징했습니다
// 필요에 따라 필드를 수정하세요
@AllArgsConstructor
@Builder
@ToString(of={"userId", "name"})
@Getter
public class UserPrincipal implements UserDetails, OAuth2User {
    private Long userId; // PK
    private String name;
    private Map<String, Object> oauth2UserAttributes;

    public static UserPrincipal create(User user, Map<String, Object> oauth2UserAttributes) {
        return new UserPrincipal(user.getUserId(), user.getUsername(), oauth2UserAttributes);
    }

    public static UserPrincipal create(User user) {
        return new UserPrincipal(user.getUserId(), user.getUsername(), new HashMap<>());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));

        return authorityList;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        // MEMIL username이 아니라 PK 값인 id를 넘겨 unique한 상태를 유지합니다
        return String.valueOf(this.userId);
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료 여부
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠김 여부
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 비밀번호 만료 여부
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정 활성화 여부
        return true;
    }

    @Override
    @Nullable
    public <A> A getAttribute(String name) {
        return (A) oauth2UserAttributes.get(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(oauth2UserAttributes);
    }

    @Override
    public String getName() {
        return this.name;
    }
}
