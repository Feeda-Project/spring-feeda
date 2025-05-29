package com.example.feeda.domain.account.dto;

import com.example.feeda.domain.account.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserResponseDTO {
    private final Long id;
    private final String email;
    private String nickName;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    // TODO: Profile 값 도 추가

    public UserResponseDTO(Account account) {
        this.id = account.getId();
        this.email = account.getEmail();
        this.createdAt = account.getCreatedAt();
        this.updatedAt = account.getUpdatedAt();
    }
}
