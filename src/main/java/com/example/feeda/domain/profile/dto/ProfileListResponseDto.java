package com.example.feeda.domain.profile.dto;

import java.util.List;

public class ProfileListResponseDto {

    // 프로필 리스트
    private final List<GetProfileResponseDto> profiles;

    // 현재 페이지 번호
    private final int currentPage;

    // 전체 페이지 수
    private final int totalPages;

    // 전체 아이템 수
    private final long totalItems;


    protected ProfileListResponseDto() {
        this.profiles = null;
        this.currentPage = 0;
        this.totalPages = 0;
        this.totalItems = 0;
    }

    /**
     * 모든 필드를 초기화하는 생성자
     */
    public ProfileListResponseDto(List<GetProfileResponseDto> profiles, int currentPage, int totalPages, long totalItems) {
        this.profiles = profiles;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    // getter 메서드들

    public List<GetProfileResponseDto> getProfiles() {
        return profiles;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalItems() {
        return totalItems;
    }
}
