package com.example.feeda.domain.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class SignUpRequestDTO {
    @NotNull(message = "이메일은 필수 항목입니다.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private String email;

    @NotNull
    @Size(min = 2, max = 20)
    private String nickName;

    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private Date birth;

    @Size(max = 1000, message = "자기소개는 1000자 이하로 작성해주세요.")
    private String bio;

    @NotNull(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 8, max = 100, message = "비밀번호는 8자 이상 100자 이하여야 합니다.")
    private String password;
}
