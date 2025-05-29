package com.example.feeda.domain.account;

import com.example.feeda.domain.account.dto.*;
import com.example.feeda.security.jwt.JwtPayload;
import com.example.feeda.security.jwt.JwtUtil;
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
    private final AccountService accountService;
    private final JwtUtil jwtUtil;

    @PostMapping("/accounts")
    public ResponseEntity<UserResponseDTO> signup(@RequestBody SignUpRequestDTO requestDTO) {
        return new ResponseEntity<>(accountService.signup(requestDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/accounts/me")
    public ResponseEntity<Void> deleteAccount(
            @AuthenticationPrincipal JwtPayload jwtPayload,
            @RequestBody DeleteAccountRequestDTO requestDTO
    ) {
        accountService.deleteAccount(jwtPayload.getProfileId(), requestDTO.getPassword());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/accounts/password")
    public ResponseEntity<UserResponseDTO> updatePassword(
            @AuthenticationPrincipal JwtPayload jwtPayload,
            @RequestBody UpdatePasswordRequestDTO requestDTO
    ) {
        return new ResponseEntity<>(accountService.updatePassword(jwtPayload.getAccountId(), requestDTO), HttpStatus.OK);
    }

    @PostMapping("/accounts/login")
    public ResponseEntity<UserResponseDTO> login(
            @RequestBody LogInRequestDTO requestDTO
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

}
