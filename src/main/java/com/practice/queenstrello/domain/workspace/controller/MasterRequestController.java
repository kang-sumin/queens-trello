package com.practice.queenstrello.domain.workspace.controller;

import com.practice.queenstrello.domain.workspace.service.MasterRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MasterRequestController {

    private final MasterRequestService masterRequestService;

    // 일반 유저가 MASTER 권한 변경 신청 API
    public ResponseEntity<String> saveMasterRequest(
            Long authUser
    ){
        return ResponseEntity.ok(masterRequestService.saveMasterRequest(authUser));
    }
}
