package com.practice.queenstrello.domain.board.dto.request;

import lombok.Getter;

@Getter
public class BoardSaveRequest {
    private Long id;
    private String title;
    private String backgroundColor;
    private String imageUrl;
}

