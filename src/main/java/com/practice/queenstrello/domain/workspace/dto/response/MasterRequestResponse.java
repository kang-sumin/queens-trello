package com.practice.queenstrello.domain.workspace.dto.response;

import com.practice.queenstrello.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MasterRequestResponse {

    private final Long id;
    private final Boolean isAccepted;
    private final LocalDateTime applicationDate;
    private final User user;

    public MasterRequestResponse(
            Long id,
            Boolean isAccepted,
            LocalDateTime applicationDate,
            User user) {
        this.id = id;
        this.isAccepted = isAccepted;
        this.applicationDate = applicationDate;
        this.user = user;
    }
}
