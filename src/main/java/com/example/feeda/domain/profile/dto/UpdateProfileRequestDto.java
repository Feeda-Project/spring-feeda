package com.example.feeda.domain.profile.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class UpdateProfileRequestDto {

    // 닉네임
    @Size(max = 50, message = "닉네임은 50자 이하로 입력해주세요.")
    private String nickname;

    //생일
    private Date birth;

    // 자기소개
    private String bio;

}
