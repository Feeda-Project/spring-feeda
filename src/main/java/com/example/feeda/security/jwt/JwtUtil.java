package com.example.feeda.security.jwt;

import com.example.feeda.exception.TokenNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(JwtPayload payload) {
        Date date = new Date();

        return Jwts.builder()
                    .setSubject(String.valueOf(payload.getAccountId()))
                    .claim("profileId", payload.getProfileId())
                    .claim("nickName", payload.getNickName())
                    .claim("email", payload.getEmail())
                    .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                    .setIssuedAt(date) // 발급일
                    .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                    .compact();
    }

    public String extractToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(BEARER_PREFIX.length()); // "Bearer " 제거 후 반환
        }

        throw new TokenNotFoundException("토큰을 찾을 수 없습니다.");
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public long getRemainingExpiration(String token) {
        Date expiration = extractClaims(token).getExpiration();
        return expiration.getTime() - System.currentTimeMillis();
    }

    public Long getAccountId(String token) {
        return Long.parseLong(extractClaims(token).getSubject());
    }

    public Long getProfileId(String token) {
        return extractClaims(token).get("profileId", Long.class);
    }

    public String getNickName(String token) {
        return extractClaims(token).get("nickName", String.class);
    }

    public String getEmail(String token) {
        return extractClaims(token).get("email", String.class);
    }
}
