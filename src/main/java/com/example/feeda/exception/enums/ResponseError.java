package com.example.feeda.exception.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseError {
    // 회원 관리 관련 오류
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일 입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    INVALID_EMAIL_OR_PASSWORD(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다."),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "계정이 존재하지 않습니다."),

    // 프로필 관련 오류
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 닉네임 입니다."),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "프로필이 존재하지 않습니다."),

    // 팔로우 관련 오류
    ALREADY_FOLLOWED(HttpStatus.BAD_REQUEST, "이미 팔로우한 계정입니다."),
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 팔로우입니다."),
    CANNOT_FOLLOW_SELF(HttpStatus.BAD_REQUEST, "본인 프로필은 팔로우/언팔로우 할 수 없습니다"),

    // 게시글 관련 오류
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다"),
    ALREADY_LIKED_POST(HttpStatus.BAD_REQUEST, "이미 좋아요한 게시글 입니다."),
    NOT_YET_LIKED_POST(HttpStatus.BAD_REQUEST, "아직 좋아요 하지 않은 게시글 입니다."),
    ALREADY_LIKED_COMMENT(HttpStatus.BAD_REQUEST, "이미 좋아요한 댓글입니다."),
    NOT_YET_LIKED_COMMENT(HttpStatus.BAD_REQUEST, "아직 좋아요 하지 않은 댓글 입니다."),

    // 페이징 & 검색 관련 오류
    INVALID_PAGINATION_PARAMETERS(HttpStatus.BAD_REQUEST, "페이지 번호는 1 이상, 페이지 크기는 1 이상이어야 합니다."),
    INVALID_DATE_PARAMETERS(HttpStatus.BAD_REQUEST, "startUpdatedAt과 endUpdatedAt은 둘 다 있어야 하거나 둘 다 없어야 합니다."),

    // 권한 관련 오류
    NO_PERMISSION_TO_EDIT(HttpStatus.FORBIDDEN, "수정 권한이 없습니다."),
    NO_PERMISSION_TO_DELETE(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ResponseError(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
