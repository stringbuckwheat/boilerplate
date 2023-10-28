package com.memil.setting.oauth2;

import com.memil.setting.entity.User;
import com.memil.setting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.debug("loadUser 호출");
        // 성공정보를 바탕으로 DefaultOAuth2UserService 객체를 만든 뒤 User를 받는다
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        // 구글 로그인인지, 카카오 로그인인지
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth2UserService를 통해 가져온 데이터를 담을 클래스
        // attribute: {name, id, key, email, picture...}
        // OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId, oAuth2User.getAttributes());

        // 기존 회원이면 update, 신규 회원이면 save
        User user = saveOrUpdate(oAuth2Attribute);

        // UserPrincipal: Authentication에 담을 OAuth2User와 (일반 로그인 용)UserDetails를 implements한 커스텀 클래스
        return UserPrincipal.create(user, oAuth2Attribute.getAttributes());
    }

    public User saveOrUpdate(OAuth2Attribute oAuth2Attribute){
        User user = userRepository.findByUsernameAndProvider(oAuth2Attribute.getEmail(), oAuth2Attribute.getProvider())
                .orElse(oAuth2Attribute.toEntity());

        return userRepository.save(user);
    }
}
