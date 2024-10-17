package com.practice.queenstrello.domain.board.dto.response;

import com.practice.queenstrello.domain.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardSimpleResponse {

    private final Long id;
    private final String title;
    private final String backgroundColor;
    private final String imageUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public BoardSimpleResponse(Long id, String title, String backgroundColor, String imageUrl, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;

    }
}