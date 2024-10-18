package com.practice.queenstrello.domain.workspace.service;

import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.common.aop.annotation.SlackMaster;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.entity.UserRole;
import com.practice.queenstrello.domain.user.repository.UserRepository;
import com.practice.queenstrello.domain.workspace.dto.request.WorkspaceMemberRequest;
import com.practice.queenstrello.domain.workspace.dto.response.MasterRequestResponse;
import com.practice.queenstrello.domain.workspace.entity.MasterRequest;
import com.practice.queenstrello.domain.workspace.entity.MemberRole;
import com.practice.queenstrello.domain.workspace.entity.WorkspaceMember;
import com.practice.queenstrello.domain.workspace.repository.MasterRequestRepository;
import com.practice.queenstrello.domain.workspace.repository.WorkspaceMemberRepository;
import com.practice.queenstrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkspaceAdminService {

    private final MasterRequestRepository masterRequestRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;


    // Master로 권한 변경
    @Transactional
    @SlackMaster
    public String updateUserRole(Long userId) {

        // todo : 변경하려는 User권한이 ROLE_USER인지 확인하여 예외 처리 하기

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.USER_NOT_FOUND));

        MasterRequest masterRequest = masterRequestRepository.findByRequestUserId(userId)
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.MASTER_REQUEST_NOT_FOUND));

        user.updateRole(UserRole.of("ROLE_MASTER"));
        masterRequest.updateIsAccepted(true);

        return String.format("%s 님의 권한이 %s로 정상적으로 변경되었습니다.", user.getNickname(), user.getUserRole());
    }

    // 승인되지 않은 Master 권한 변경 요청 내역 조회 (다건 조회)
    public Page<MasterRequestResponse> getMasterRequests(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<MasterRequest> masterRequests = masterRequestRepository.findAllByIsAcceptedFalse(pageable)
                .filter(mr -> !mr.isEmpty())
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.MASTER_REQUEST_NOT_EXIST));

        return masterRequests.map(mr -> new MasterRequestResponse(
                mr.getId(),
                mr.getIsAccepted(),
                mr.getCreatedAt(),
                mr.getRequestUser()
        ));
    }


    // Member 권한 변경
    @Transactional
    public String updateMemberRole(Long workspaceId, Long userId, WorkspaceMemberRequest workspaceMemberRequest) {

        // 권한을 변경하고자 하는 사용자 객체 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.USER_NOT_FOUND));

        // 변경하고자 하는 사용자의 권한이 USER 인지 확인
        if (!user.getUserRole().equals(UserRole.of("ROLE_USER"))) {
            throw new QueensTrelloException(ErrorCode.USER_HAS_NOT_PERMISSION);
        }

        // 워크스페이스가 존재하는지 확인
        workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.WORKSPACE_NOT_FOUND));

        // 변경하고자 하는 사용자가 변경하고자 하는 워크스페이스 멤버로 존재하는지 확인
        if (!(workspaceMemberRepository.existsByMemberIdAndWorkspaceId(userId, workspaceId))) {
            throw new QueensTrelloException(ErrorCode.WORKSPACE_MEMBER_NOT_FOUND);
        }

        WorkspaceMember workspaceMember = workspaceMemberRepository.findByMemberIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.WORKSPACE_MEMBER_NOT_FOUND));

        // 변경하고자하는 멤버권한이 기존 권한과 같을 경우 예외 처리
        if (workspaceMember.getMemberRole().equals(MemberRole.of(workspaceMemberRequest.getMemberRole()))) {
            throw new QueensTrelloException(ErrorCode.SAME_EXIST_MEMBER_ROLE);
        }

        workspaceMember.updateMemberRole(MemberRole.of(workspaceMemberRequest.getMemberRole()));

        return String.format("%s 님의 권한이 %s 로 정상적으로 변경되었습니다.", user.getNickname(), workspaceMember.getMemberRole());
    }
}
