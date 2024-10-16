package com.practice.queenstrello.domain.workspace.service;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.entity.UserRole;
import com.practice.queenstrello.domain.workspace.dto.request.WorkspaceSaveRequest;
import com.practice.queenstrello.domain.workspace.dto.response.WorkspaceResponse;
import com.practice.queenstrello.domain.workspace.entity.Workspace;
import com.practice.queenstrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    // 워크 스페이스 생성
    @Transactional
    public WorkspaceResponse saveWorkspace(AuthUser authUser, WorkspaceSaveRequest workspaceSaveRequest) {

        User user = User.fromAuthUser(authUser);

        // 요청한 사용자의 권한이 ADMIN 또는 MASTER인지 확인
        if (user.getUserRole().equals(UserRole.ROLE_USER)) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION);
        }

        Workspace newWorkspace = new Workspace(
                workspaceSaveRequest.getName(),
                workspaceSaveRequest.getDescription(),
                user
        );
        Workspace savedWorkspace = workspaceRepository.save(newWorkspace);

        return new WorkspaceResponse(
                savedWorkspace.getId(),
                savedWorkspace.getName(),
                savedWorkspace.getDescription(),
                savedWorkspace.getCreatedAt(),
                savedWorkspace.getMasterUser(),
                savedWorkspace.getCreateUser()
        );
    }
}
