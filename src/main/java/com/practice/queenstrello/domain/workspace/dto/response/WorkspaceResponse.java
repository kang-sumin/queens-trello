package com.practice.queenstrello.domain.workspace.dto.response;

import com.practice.queenstrello.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WorkspaceResponse {

    private final Long workspaceId;
    private final String workspaceName;
    private final String workspaceDescription;
    private final LocalDateTime createdAt;
    private final String masterUserName;
    private final String createUserName;


    public WorkspaceResponse(Long workspaceId, String workspaceName, String workspaceDescription, LocalDateTime createdAt, User masterUser, User createUser) {
        this.workspaceId = workspaceId;
        this.workspaceName = workspaceName;
        this.workspaceDescription = workspaceDescription;
        this.createdAt = createdAt;
        this.masterUserName = masterUser.getNickname();
        this.createUserName = createUser.getNickname();
    }
}
