package com.practice.queenstrello.domain.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentSaveRequest {
    private String content; //댓글 내용
    private Long userId; //댓글 작성자
}
