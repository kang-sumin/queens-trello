package com.practice.queenstrello.domain.card.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CardUpdateResponse {
    private final Long cardId;
    private final String title;
    private final String content;
    private final LocalDateTime deadLine;
    private final List<Long> managerIds;

    public CardUpdateResponse(Long cardId,String title, String content, LocalDateTime deadLine, List<Long> managerIds) {
        this.cardId=cardId;
        this.title = title;
        this.content = content;
        this.deadLine = deadLine;
        this.managerIds = managerIds;
    }
}
