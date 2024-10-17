package com.practice.queenstrello.domain.workspace.service;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.entity.UserRole;
import com.practice.queenstrello.domain.user.repository.UserRepository;
import com.practice.queenstrello.domain.workspace.dto.request.WorkspaceMemberEmailRequest;
import com.practice.queenstrello.domain.workspace.dto.request.WorkspaceRequest;
import com.practice.queenstrello.domain.workspace.dto.response.WorkspaceResponse;
import com.practice.queenstrello.domain.workspace.dto.response.WorkspaceUpdateResponse;
import com.practice.queenstrello.domain.workspace.entity.MemberRole;
import com.practice.queenstrello.domain.workspace.entity.Workspace;
import com.practice.queenstrello.domain.workspace.entity.WorkspaceMember;
import com.practice.queenstrello.domain.workspace.repository.WorkspaceMemberRepository;
import com.practice.queenstrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final UserRepository userRepository;

    // 워크 스페이스 생성
    @Transactional
    public WorkspaceResponse saveWorkspace(AuthUser authUser, WorkspaceRequest workspaceRequest) {

        User user = User.fromAuthUser(authUser);

        // 요청한 사용자의 권한이 ADMIN 또는 MASTER인지 확인
        if (user.getUserRole().equals(UserRole.ROLE_USER)) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION);
        }

        Workspace newWorkspace = new Workspace(
                workspaceRequest.getName(),
                workspaceRequest.getDescription(),
                user
        );
        Workspace savedWorkspace = workspaceRepository.save(newWorkspace);

        // 사용자를 워크스페이스 (WORKSPACE) 권한 멤버로 추가
        WorkspaceMember newWorkspaceMember = new WorkspaceMember(
                MemberRole.WORKSPACE,
                user,
                savedWorkspace
        );
        workspaceMemberRepository.save(newWorkspaceMember);

        return new WorkspaceResponse(
                savedWorkspace.getId(),
                savedWorkspace.getName(),
                savedWorkspace.getDescription(),
                savedWorkspace.getCreatedAt(),
                savedWorkspace.getMasterUser(),
                savedWorkspace.getCreateUser()
        );
    }

    // 워크 스페이스에 멤버 초대
    @Transactional
    public String addMember(Long workspaceId, AuthUser authUser, WorkspaceMemberEmailRequest workspaceMemberEmailRequest) {

        // 로그인한 사용자
        User user = User.fromAuthUser(authUser);

        // 로그인 사용자가 USER 권한 일때 MemberRole이 WORKSPACE가 아닐때 예외 처리
        if (user.getUserRole().equals(UserRole.ROLE_USER) && !(workspaceMemberRepository.existsByMemberIdAndWorkspaceIdAndMemberRole(authUser.getUserId(), workspaceId, MemberRole.WORKSPACE))) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION);
        }

        // 초대하고자 하는 워크스페이스 검색
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.WORKSPACE_NOT_FOUND));

        // 이메일을 사용하여 사용자 검색
        User inviteMember = userRepository.findByEmail(workspaceMemberEmailRequest.getEmail())
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.USER_NOT_FOUND));


        // 사용자를 워크스페이스 멤버로 추가
        WorkspaceMember newWorkspaceMember = new WorkspaceMember(
                MemberRole.READ,
                inviteMember,
                workspace
        );
        workspaceMemberRepository.save(newWorkspaceMember);

        return String.format("%s 님을 %s 워크스페이스의 멤버로 초대하였습니다.", inviteMember.getNickname(), workspace.getName());
    }

    // 워크 스페이스 수정
    @Transactional
    public WorkspaceUpdateResponse updateWorkspace(Long workspaceId, AuthUser authUser, WorkspaceRequest workspaceRequest) {

        // 서비스 권한 확인
        User user = checkPermission(authUser, workspaceId);

        // 수정하고자 하는 워크스페이스 검색
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.WORKSPACE_NOT_FOUND));

        workspace.update(workspaceRequest);

        return new WorkspaceUpdateResponse(
                workspace.getId(),
                workspace.getName(),
                workspace.getDescription(),
                workspace.getCreatedAt(),
                workspace.getModifiedAt(),
                workspace.getMasterUser(),
                workspace.getCreateUser(),
                user
        );
    }

    // 워크스페이스 조회 (유저가 멤버로 가입된 자신의 워크스페이스 목록을 확인할 수 있음)
    public Page<WorkspaceResponse> getUserWorkspace(int page, int size, AuthUser authUser) {
        Pageable pageable = PageRequest.of(page - 1, size);

        User user = User.fromAuthUser(authUser);

        // 현재 로그인된 사용자의 ID로 등록된 WorspaceID 검색
        List<Long> workspaceIds = workspaceMemberRepository.findWorkspaceIdByMemberId(user.getId())
                .filter(list -> !list.isEmpty())    // 리스트가 비어있는지 확인
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.WORKSPACE_NOT_FOUND));

        Page<Workspace> workspaces = workspaceRepository.findByWorkspaceId(workspaceIds, pageable);

        return workspaces.map(workspace -> new WorkspaceResponse(
                workspace.getId(),
                workspace.getName(),
                workspace.getDescription(),
                workspace.getCreatedAt(),
                workspace.getMasterUser(),
                workspace.getCreateUser()
        ));
    }


    // 워크 스페이스 삭제
    @Transactional
    public String deleteWorkspace(Long workspaceId, AuthUser authUser) {

        User user = checkPermission(authUser, workspaceId);

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.WORKSPACE_NOT_FOUND));

        workspaceRepository.delete(workspace);

        return String.format("%s 가 {%s} 워크스페이스를 삭제하였습니다.", user.getNickname(), workspace.getName());
    }


    // 워크스페이스 서비스 권한 확인
    private User checkPermission(AuthUser authUser, Long workspaceId) {
        // 로그인한 사용자
        User user = User.fromAuthUser(authUser);

        // 로그인 사용자가 USER 권한 일때 MemberRole이 WORKSPACE가 아닐때 예외 처리
        if (user.getUserRole().equals(UserRole.ROLE_USER) && !(workspaceMemberRepository.existsByMemberIdAndWorkspaceIdAndMemberRole(authUser.getUserId(), workspaceId, MemberRole.WORKSPACE))) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION);
        }

        return user;
    }


}
