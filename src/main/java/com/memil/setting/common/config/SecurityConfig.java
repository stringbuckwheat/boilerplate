package com.memil.setting.common.config;

import com.memil.setting.auth.filter.CustomAuthenticationEntryPoint;
import com.memil.setting.auth.filter.JwtExceptionFilter;
import com.memil.setting.auth.filter.JwtFilter;
import com.memil.setting.auth.oauth2.OAuth2MemberService;
import com.memil.setting.auth.oauth2.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

// Spring Security 설정 클래스
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final OAuth2SuccessHandler successHandler;
    private final OAuth2MemberService oAuth2MemberService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic(HttpBasicConfigurer::disable) // MEMIL: 로그인창
//                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/api/login", "/api/user/username", "/api/user", "/api/token/reissue", "/api/logout").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Configurer ->
                        oauth2Configurer
                                .successHandler(successHandler)
                                .userInfoEndpoint(configurer -> configurer.userService(oAuth2MemberService))
                )
                .exceptionHandling(e -> e.authenticationEntryPoint(authenticationEntryPoint))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtFilter.class);

        return httpSecurity.build();
    }

    //    // CORS 설정 메소드 (필요시 주석 해제)
//    CorsConfigurationSource corsConfigurationSource() {
//        return request -> {
//            CorsConfiguration config = new CorsConfiguration();
//            config.setAllowedHeaders(Collections.singletonList("*"));
//            config.setAllowedMethods(Collections.singletonList("*"));
//            config.setAllowedOriginPatterns(Collections.singletonList("http://localhost:3000"));
//            config.setAllowCredentials(true);
//
//            return config;
//        };
//    }
}
