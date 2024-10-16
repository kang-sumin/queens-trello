package com.practice.queenstrello.domain.notify.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SlackMessageRequest {
    @NotEmpty
    private String title;

    @NotEmpty
    private String message;
}
