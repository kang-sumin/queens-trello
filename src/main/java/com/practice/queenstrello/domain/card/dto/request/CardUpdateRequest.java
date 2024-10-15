package com.practice.queenstrello.domain.card.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CardUpdateRequest {
    private String title;
    private String content;
    private LocalDateTime deadLine;
    private List<Long> managerIds;
}
