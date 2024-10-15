package com.practice.queenstrello.domain.workspace.service;

import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.entity.UserRole;
import com.practice.queenstrello.domain.workspace.entity.MasterRequest;
import com.practice.queenstrello.domain.workspace.repository.MasterRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MasterRequestService {

    private final UserRepository userRepository;
    private final MasterRequestRepository masterRequestRepository;

    // 일반 유저가 MASTER 권한 변경 신청 객체 저장
    @Transactional
    public String saveMasterRequest(Long authUser) {
        String responseMessage = "정상적으로 요청이 신청되었습니다.";

        User requestUser = findUserByUserId(authUser.getId());

        // 요청한 사용자의 권한이 USER인지 확인
        if(!requestUser.getUserRole().equals(UserRole.ROLE_USER)) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION_MASTER_REQUEST);
        }

        MasterRequest newMasterRequest = new MasterRequest(
                false,
                requestUser
        );
        masterRequestRepository.save(newMasterRequest);

        return responseMessage;
    }

    // 사용자 검색
    private User findUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new QueensTrelloException(ErrorCode.NOT_FOUND));
    }

}
