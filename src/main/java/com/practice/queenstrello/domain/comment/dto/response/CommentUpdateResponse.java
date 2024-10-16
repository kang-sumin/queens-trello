package com.practice.queenstrello.domain.comment.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentUpdateResponse {
    private final Long id;
    private final String content;
    private final LocalDateTime modigiedAt;

    public CommentUpdateResponse(Long id, String content, LocalDateTime modigiedAt) {
        this.id = id;
        this.content = content;
        this.modigiedAt = modigiedAt;
    }
}
