package com.practice.queenstrello.domain.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentUpdateRequest {
    private String content;
}
