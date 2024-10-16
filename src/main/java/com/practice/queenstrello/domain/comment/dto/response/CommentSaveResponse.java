package com.practice.queenstrello.domain.comment.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentSaveResponse {
    private final Long commentId;
    private final String content;
    private final Long userId;
    private final LocalDateTime createdAt;

    public CommentSaveResponse(Long commentId, String content, Long userId, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;
    }
}
