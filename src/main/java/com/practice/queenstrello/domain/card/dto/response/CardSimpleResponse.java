package com.practice.queenstrello.domain.card.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CardSimpleResponse {
    private final String title;
    private final String content;
    private final LocalDateTime deadLine;
    private final LocalDateTime createdAt;
    private final List<Long> managerIds; //담당자 ID목록

    public CardSimpleResponse(String title, String content, LocalDateTime deadLine, LocalDateTime createdAt, List<Long> managerIds) {
        this.title = title;
        this.content = content;
        this.deadLine = deadLine;
        this.createdAt = createdAt;
        this.managerIds = managerIds;
    }
}
