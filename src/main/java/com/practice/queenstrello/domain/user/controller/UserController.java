package com.practice.queenstrello.domain.user.controller;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.user.dto.request.UserDeletionRequest;
import com.practice.queenstrello.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원탈퇴
    @DeleteMapping("/users/signout")
    public ResponseEntity<String> deleteUser(
            // 현재 로그인된 사용자 정보를 가져오는 부분
            @AuthenticationPrincipal AuthUser authUser,
            // 사용자가 입력한 비밀번호를 받아오는 부분
            @RequestBody UserDeletionRequest deletionRequest
    ) {
        // 회원 탈퇴 로직을 처리 부분
        userService.deleteUser(authUser.getUserId(), deletionRequest.getPassword());
        return ResponseEntity.ok("회원탈퇴가 완료되었습니다.");
    }
}