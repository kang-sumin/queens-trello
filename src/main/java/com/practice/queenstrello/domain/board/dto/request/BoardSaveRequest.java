package com.practice.queenstrello.domain.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BoardSaveRequest {
    @NotBlank
    private String title;
    private String backgroundColor;
    private String imageUrl;
}

