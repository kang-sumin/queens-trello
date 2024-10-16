package com.practice.queenstrello.domain.workspace.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceSaveRequest {

    @NotBlank
    private String name;
    private String description;

}
