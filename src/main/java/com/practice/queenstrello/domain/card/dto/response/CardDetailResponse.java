package com.practice.queenstrello.domain.card.dto.response;

import com.practice.queenstrello.domain.comment.dto.response.CommentSaveResponse;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CardDetailResponse {
    private final String title;
    private final String content;
    private final LocalDateTime deadLine;
    private final List<Long> managerIds;
    private final List<CommentSaveResponse> comments;

    public CardDetailResponse(String title, String content, LocalDateTime deadLine, List<Long> managerIds, List<CommentSaveResponse> comments) {
        this.title = title;
        this.content = content;
        this.deadLine = deadLine;
        this.managerIds = managerIds;
        this.comments = comments;
    }
}
