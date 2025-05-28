package com.example.feeda.domain.profile.dto;

import java.util.List;

public class ProfileListResponseDto {

    private List<GetProfileResponseDto> profiles;
    private int currentPage;
    private int totalPages;
    private long totalItems;

    /**
     * JPA에서 기본으로 사용되는 기본 생성자
     */

    public ProfileListResponseDto() {}

    public ProfileListResponseDto(List<GetProfileResponseDto> profiles, int currentPage, int totalPages, long totalItems) {
        this.profiles = profiles;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    // getters & setters

    public List<GetProfileResponseDto> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<GetProfileResponseDto> profiles) {
        this.profiles = profiles;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

}
