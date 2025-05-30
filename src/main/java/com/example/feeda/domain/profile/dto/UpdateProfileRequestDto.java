package com.example.feeda.domain.profile.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Date;

@AllArgsConstructor(staticName = "of")
@Getter
public class UpdateProfileRequestDto {

    // 닉네임
    @Size(max = 50, message = "닉네임은 50자 이하로 입력해주세요.")
    private final String nickname;

    //생일
    private final Date birth;

    // 자기소개
    private final String bio;

}
