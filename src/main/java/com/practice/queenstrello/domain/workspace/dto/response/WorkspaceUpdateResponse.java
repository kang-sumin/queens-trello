package com.practice.queenstrello.domain.workspace.dto.response;

import com.practice.queenstrello.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WorkspaceUpdateResponse {

    private final Long workspaceId;
    private final String workspaceName;
    private final String workspaceDescription;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final String masterUserName;
    private final String createUserName;
    private final String modifiedUserName;


    public WorkspaceUpdateResponse(Long workspaceId, String workspaceName, String workspaceDescription, LocalDateTime createdAt, LocalDateTime modifiedAt, User masterUser, User createUser, User modifiedUser) {
        this.workspaceId = workspaceId;
        this.workspaceName = workspaceName;
        this.workspaceDescription = workspaceDescription;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.masterUserName = masterUser.getNickname();
        this.createUserName = createUser.getNickname();
        this.modifiedUserName = modifiedUser.getNickname();
    }
}
