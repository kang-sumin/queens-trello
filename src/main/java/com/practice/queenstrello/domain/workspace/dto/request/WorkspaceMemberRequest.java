package com.practice.queenstrello.domain.workspace.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class WorkspaceMemberRequest {

    @NotBlank
    private String memberRole;
}
