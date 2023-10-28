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

@AllArgsConstructor
@Builder
@ToString
@Getter
public class UserPrincipal implements UserDetails, OAuth2User {
    private Long userId;
    private String name;
    private Map<String, Object> oauth2UserAttributes;

    public UserPrincipal(Long id, String name) {
        this.userId = id;
        this.name = name;
    }

    public UserPrincipal(String id) {
        this.userId = Long.parseLong(id);
    }

    /* OAuth2 로그인 사용 */
    public static UserPrincipal create(User user, Map<String, Object> oauth2UserAttributes) {
        return new UserPrincipal(user.getUserId(), user.getName(), oauth2UserAttributes);
    }

    /* 일반 로그인 사용 */
    public static UserPrincipal create(User user) {
        return new UserPrincipal(user.getUserId(), user.getName(), new HashMap<>());
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
        // username이 아니라 PK 값인 id를 넘겨준다
        // email은 중복 가능
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
