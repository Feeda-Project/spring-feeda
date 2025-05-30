package com.example.feeda.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String encode(String password) {
        return encoder.encode(password);
    }

    // 경고 무시: 부울 메서드 'matches()'에 대한 호출은 항상 반전됩니다
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean matches(String password, String encodedPassword) {
        return encoder.matches(password, encodedPassword);
    }
}