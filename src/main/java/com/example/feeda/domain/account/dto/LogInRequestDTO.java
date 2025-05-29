package com.example.feeda.domain.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogInRequestDTO {
    @NotNull(message = "이메일은 필수 항목입니다.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private String email;

    @NotNull(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 8, max = 100, message = "비밀번호는 8자 이상 100자 이하여야 합니다.")
    private String password;
}
