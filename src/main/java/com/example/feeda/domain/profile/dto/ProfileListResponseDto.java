package com.example.feeda.domain.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor(staticName = "of")
@Getter
public class ProfileListResponseDto {

    // 프로필 리스트
    private final List<GetProfileResponseDto> profiles;

    // 현재 페이지 번호
    private final int currentPage;

    // 전체 페이지 수
    private final int totalPages;

    // 전체 아이템 수
    private final long totalItems;

}
