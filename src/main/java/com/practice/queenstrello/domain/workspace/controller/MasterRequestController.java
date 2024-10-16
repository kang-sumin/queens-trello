package com.practice.queenstrello.domain.workspace.controller;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.workspace.service.MasterRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MasterRequestController {

    private final MasterRequestService masterRequestService;

    // 일반 유저가 MASTER 권한 변경 신청 API
    @PostMapping("/users/master-request")
    public ResponseEntity<String> saveMasterRequest(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        return ResponseEntity.ok(masterRequestService.saveMasterRequest(authUser));
    }
}
