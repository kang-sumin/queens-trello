package com.practice.queenstrello.domain.workspace.service;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.entity.UserRole;
import com.practice.queenstrello.domain.user.repository.UserRepository;
import com.practice.queenstrello.domain.workspace.dto.response.MasterRequestResponse;
import com.practice.queenstrello.domain.workspace.entity.MasterRequest;
import com.practice.queenstrello.domain.workspace.repository.MasterRequestRepository;
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


    // Master로 권한 변경
    @Transactional
    public String updateUserRole(Long userId, AuthUser authUser) {

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
    public Page<MasterRequestResponse> getMasterRequests(int page, int size, AuthUser authUser) {
        Pageable pageable = PageRequest.of(page - 1, size);

//        Page<MasterRequest> masterRequests = findMasterRequests()

        return null;
    }


}
