package com.example.feeda.config;

import com.example.feeda.filter.JwtFilter;
import com.example.feeda.security.jwt.JwtBlacklistService;
import com.example.feeda.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                // 세션 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 접근 제어
                .authorizeHttpRequests(auth -> auth
                        // 회원가입, 로그인은 인증 제외
                        .requestMatchers("/api/accounts", "/api/accounts/login").permitAll()

                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/**").authenticated()

                        // 비로그인 시 GET 만 허용
//                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
//                        .requestMatchers("/api/**").permitAll()

                        .anyRequest().denyAll()
                )

                // 필터 등록
                .addFilterBefore(new JwtFilter(jwtBlacklistService, jwtUtil), UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(configurer ->
                        configurer
                                .authenticationEntryPoint((request, response, authException) -> {
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    response.setContentType("application/json;charset=UTF-8");

                                    String message = "{\"error\": \"인증 실패: " + authException.getMessage() + "\"}";
                                    response.getWriter().write(message);
                                })
                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                    response.setContentType("application/json;charset=UTF-8");

                                    String message = "{\"error\": \"접근 거부: " + accessDeniedException.getMessage() + "\"}";
                                    response.getWriter().write(message);
                                })
                )

                .build();
    }
}
