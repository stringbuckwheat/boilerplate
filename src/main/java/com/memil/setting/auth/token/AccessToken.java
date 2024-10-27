package com.memil.setting.auth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.ToString;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@ToString
public class AccessToken {
    // 만료 시간(분)
    public static final int EXPIRED_AFTER = 15;

    // 암호화된 token
    private final String token;

    // key
    private final Key key;

    // 만료 일자
    private LocalDateTime expiredAt;

    public AccessToken(Long id, String username, Key key) {
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(EXPIRED_AFTER);
        Date expiredDate = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());

        // claims 만들기
        Map<String, Object> claims = new HashMap<>();

        claims.put("iss", "skeleton"); // 발행인
        claims.put("aud", username); // 토큰 대상자(User PK)
        claims.put("exp", LocalDateTime.now().toString()); // 발행 시간

        this.key = key;
        this.expiredAt = expiredAt;
        this.token = createJwtAuthToken(id, claims, expiredDate);
    }

    // JWT로 만드는 메소드
    public String createJwtAuthToken(Long id, Map<String, Object> claimMap, Date expiredDate) {
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .addClaims(claimMap)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiredDate)
                .compact();
    }

    // 이하는 AccessToken 유효성 검증에 사용
    public AccessToken(String token, Key key) {
        this.token = token;
        this.key = key;
    }

    // JWT 디코딩
    public Claims getData() {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
