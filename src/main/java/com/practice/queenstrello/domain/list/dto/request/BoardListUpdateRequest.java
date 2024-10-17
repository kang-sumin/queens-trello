package com.practice.queenstrello.domain.list.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BoardListUpdateRequest {
    @NotBlank
    private String title;

    @Min(1) //int로 받아도 된다. min 은 최소한 1이상의 것만 받겠다는 뜻
    private Integer order;
}
