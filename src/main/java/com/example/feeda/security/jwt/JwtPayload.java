package com.example.feeda.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtPayload {
    private final Long accountId;
    private final Long profileId;
    private final String email;
    private final String nickName;
}
