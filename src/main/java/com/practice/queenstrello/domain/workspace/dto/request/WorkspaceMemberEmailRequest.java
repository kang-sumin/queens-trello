package com.practice.queenstrello.domain.workspace.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class WorkspaceMemberEmailRequest {

    @NotBlank
    @Email
    private String email;

}
