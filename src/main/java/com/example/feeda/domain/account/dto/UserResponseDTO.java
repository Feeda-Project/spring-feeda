package com.example.feeda.domain.account.dto;

import com.example.feeda.domain.account.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserResponseDTO {
    private final Long accountId;
    private final Long profileId;
    private final String email;
    private String nickName;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public UserResponseDTO(Account account) {
        this.accountId = account.getId();
        this.profileId = account.getProfile().getId();
        this.email = account.getEmail();
        this.nickName = account.getProfile().getNickname();
        this.createdAt = account.getCreatedAt();
        this.updatedAt = account.getUpdatedAt();
    }
}
