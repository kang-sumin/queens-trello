package com.practice.queenstrello.domain.card.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardSaveRequest {
    private String title;
    private String content;
    private LocalDateTime deadLine;
    private List<Long> managerIds;

}
