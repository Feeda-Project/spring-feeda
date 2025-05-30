DROP DATABASE IF EXISTS newsFeed;
CREATE DATABASE IF NOT EXISTS newsFeed;
USE newsFeed;

-- 사용자 인증(accounts) 테이블 생성
CREATE TABLE accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 인증 ID (PK)',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT '이메일',
    password VARCHAR(255) NOT NULL COMMENT '비밀번호',
    created_at DATETIME COMMENT '생성일',
    updated_at DATETIME COMMENT '수정일'
) COMMENT = '사용자 인증 Table';

-- 사용자 정보(profile) 테이블 생성
CREATE TABLE profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 정보 ID (PK)',
    account_id BIGINT COMMENT '사용자 인증 ID (FK)',
    nickname VARCHAR(50) NOT NULL UNIQUE COMMENT '닉네임',
    birth DATE COMMENT '생년월일',
    bio TEXT COMMENT '자기소개',
    created_at DATETIME COMMENT '생성일',
    updated_at DATETIME COMMENT '수정일',

    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
) COMMENT = '사용자 정보 Table';

-- 팔로우 목록(follows) 테이블 생성
CREATE TABLE follows (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '팔로우 ID (PK)',
    follower_id BIGINT NOT NULL COMMENT '팔로우한 사람 ID (PK)',
    following_id BIGINT NOT NULL COMMENT '팔로잉된 사람 ID (FK)',
    created_at DATETIME COMMENT '생성일',

    FOREIGN KEY (follower_id) REFERENCES profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (following_id) REFERENCES profiles(id) ON DELETE CASCADE
) COMMENT = '팔로우 목록 Table';

-- 게시글(posts) 테이블 생성
CREATE TABLE posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '게시글 ID (PK)',
    profile_id BIGINT NOT NULL COMMENT '작성자 ID (FK)',
    title VARCHAR(100) NOT NULL COMMENT '제목',
    content TEXT NOT NULL COMMENT '내용',
    category VARCHAR(50) COMMENT '카테고리',
    created_at DATETIME COMMENT '생성일',
    updated_at DATETIME COMMENT '수정일',

    FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE
) COMMENT = '게시글 Table';

-- 게시글 댓글(post_comments) 테이블 생성
CREATE TABLE post_comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '댓글 ID (PK)',
    post_id BIGINT NOT NULL COMMENT '게시글 ID (FK)',
    profile_id BIGINT NOT NULL COMMENT '작성자 ID (FK)',
    content TEXT NOT NULL COMMENT '내용',
    created_at DATETIME COMMENT '생성일',
    updated_at DATETIME COMMENT '수정일',

    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE
) COMMENT = '게시글 댓글 Table';

-- 게시글 좋아요(post_likes) 테이블 생성
CREATE TABLE post_likes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '댓글 ID (PK)',
    post_id BIGINT NOT NULL COMMENT '게시글 ID (FK)',
    profile_id BIGINT NOT NULL COMMENT '사용자 ID (FK)',
    created_at DATETIME COMMENT '생성일',

    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE
) COMMENT = '게시글 좋아요 Table';

-- 게시글 댓글 좋아요(post_comment_likes) 테이블 생성
CREATE TABLE post_comment_likes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '댓글 ID (PK)',
    post_comment_id BIGINT NOT NULL COMMENT '게시글 ID (FK)',
    profile_id BIGINT NOT NULL COMMENT '사용자 ID (FK)',
    created_at DATETIME COMMENT '생성일',

    FOREIGN KEY (post_comment_id) REFERENCES post_comments(id) ON DELETE CASCADE,
    FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE
) COMMENT = '게시글 댓글 좋아요 Table';