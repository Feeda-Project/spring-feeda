package com.example.feeda.filter;

import com.example.feeda.exception.JwtValidationException;
import com.example.feeda.security.jwt.JwtBlacklistService;
import com.example.feeda.security.jwt.JwtPayload;
import com.example.feeda.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws ServletException, IOException {

        String bearerJwt = request.getHeader("Authorization");

        if (bearerJwt == null) {
            chain.doFilter(request, response);
            return;
        }

        String jwt = jwtUtil.extractToken(bearerJwt);

        try {
            // JWT 유효성 검사와 claims 추출
            Claims claims = jwtUtil.extractClaims(jwt);
            if (claims == null) {
                throw new JwtValidationException("잘못된 JWT 토큰입니다.", HttpServletResponse.SC_BAD_REQUEST);
            }

            // 블랙리스트 검증
            if (jwtBlacklistService.isBlacklisted(jwt)) {
                throw new JwtValidationException("로그아웃된 토큰입니다.", HttpServletResponse.SC_UNAUTHORIZED);
            }

            Long accountId = jwtUtil.getAccountId(jwt);
            Long profileId = jwtUtil.getProfileId(jwt);
            String nickName = jwtUtil.getNickName(jwt);
            String email = jwtUtil.getEmail(jwt);
            JwtPayload payload = new JwtPayload(accountId, profileId, email, nickName);

            // 인증 객체 생성: 사용자 정보(payload), 패스워드(""), 권한 목록(empty)
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                payload, null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );

            // 인증 정보 등록
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            chain.doFilter(request, response);

        } catch (SecurityException | MalformedJwtException e) {
            throw new JwtValidationException("유효하지 않는 JWT 서명입니다.", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            throw new JwtValidationException("만료된 JWT 토큰입니다.", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (UnsupportedJwtException e) {
            throw new JwtValidationException("지원되지 않는 JWT 토큰입니다.", HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            throw new JwtValidationException("내부 서버 오류", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
