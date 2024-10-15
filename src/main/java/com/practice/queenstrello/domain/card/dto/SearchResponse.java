package com.practice.queenstrello.domain.card.dto;

import com.practice.queenstrello.domain.card.entity.CardManager;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SearchResponse {
    private String title;
    private String content;
    private LocalDateTime deadLine;
    private List<ManagerResponse> cardManagers;

    public SearchResponse(String title, String content, LocalDateTime deadLine, List<CardManager> cardManagers) {
        this.title = title;
        this.content = content;
        this.deadLine = deadLine;
        this.cardManagers = cardManagers.stream().map(manager->new ManagerResponse(manager.getManager().getNickname())).toList();
    }
}
