package com.practice.queenstrello.domain.card.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CardSaveResponse {

    private final Long cardId;
    private final String title;
    private final String content;
    private final LocalDateTime deadline;
    private List<Long> managerIds; //담당자 이름 리스트

    public CardSaveResponse(Long cardId, String title, String content, LocalDateTime deadline, List<Long> managerIds) {
        this.cardId = cardId;
        this.title = title;
        this.content = content;
        this.deadline = deadline;
        this.managerIds=managerIds;
    }
}
