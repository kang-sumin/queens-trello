package com.practice.queenstrello.domain.workspace.service;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.entity.UserRole;
import com.practice.queenstrello.domain.user.repository.UserRepository;
import com.practice.queenstrello.domain.workspace.entity.MasterRequest;
import com.practice.queenstrello.domain.workspace.repository.MasterRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MasterRequestService {

    private final MasterRequestRepository masterRequestRepository;

    // 일반 유저가 MASTER 권한 변경 신청 객체 저장
    @Transactional
    public String saveMasterRequest(AuthUser authUser) {

        //현재 로그인 되어있는 사용자 정보 객체
        User requestUser = User.fromAuthUser(authUser);

        // 요청한 사용자의 권한이 USER인지 확인
        if (!requestUser.getUserRole().equals(UserRole.ROLE_USER)) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION_MASTER_REQUEST);
        }

        // Master 권한 요청 내역이 있는지 확인
        if(masterRequestRepository.existsByRequestUserId(authUser.getUserId())) {
            throw new QueensTrelloException(ErrorCode.MASTER_REQUEST_ALREADY_EXIST);
        }

        MasterRequest newMasterRequest = new MasterRequest(
                false,
                requestUser
        );
        masterRequestRepository.save(newMasterRequest);


        return String.format("%s 님의 MASTER 권한 변경 요청이 정상적으로 신청되었습니다.", requestUser.getNickname());
    }



}
