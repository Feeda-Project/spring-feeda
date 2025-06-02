package com.example.feeda.domain.account.controller;

import com.example.feeda.domain.account.sevice.AccountServiceImpl;
import com.example.feeda.domain.account.dto.*;
import com.example.feeda.security.jwt.JwtBlacklistService;
import com.example.feeda.security.jwt.JwtPayload;
import com.example.feeda.security.jwt.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {
    private final AccountServiceImpl accountService;
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtUtil jwtUtil;

    @PostMapping("/accounts")
    public ResponseEntity<UserResponseDTO> signup(@RequestBody @Valid SignUpRequestDTO requestDTO) {
        return new ResponseEntity<>(accountService.signup(requestDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/accounts/me")
    public ResponseEntity<Void> deleteAccount(
            @RequestHeader("Authorization") String bearerToken,
            @AuthenticationPrincipal JwtPayload jwtPayload,
            @RequestBody @Valid DeleteAccountRequestDTO requestDTO
    ) {
        accountService.deleteAccount(jwtPayload.getAccountId(), requestDTO.getPassword());

        // 토큰 무효화
        invalidateToken(bearerToken);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/accounts/password")
    public ResponseEntity<UserResponseDTO> updatePassword(
            @AuthenticationPrincipal JwtPayload jwtPayload,
            @RequestBody @Valid UpdatePasswordRequestDTO requestDTO
    ) {
        return new ResponseEntity<>(accountService.updatePassword(jwtPayload.getAccountId(), requestDTO), HttpStatus.OK);
    }

    @PostMapping("/accounts/login")
    public ResponseEntity<UserResponseDTO> login(
            @RequestBody @Valid LogInRequestDTO requestDTO
    ) {
        UserResponseDTO responseDTO = accountService.login(requestDTO);

        JwtPayload payload = new JwtPayload(
                responseDTO.getAccountId(),
                responseDTO.getProfileId(),
                responseDTO.getEmail(),
                responseDTO.getNickName()
        );

        String jwt = jwtUtil.createToken(payload);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwt);

        return new ResponseEntity<>(responseDTO, headers, HttpStatus.OK);
    }

    @PostMapping("/accounts/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String bearerToken) {
        // 토큰 무효화
        invalidateToken(bearerToken);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    private void invalidateToken(String bearerToken) {
        String token = jwtUtil.extractToken(bearerToken);
        jwtBlacklistService.addBlacklist(token);
    }
}
