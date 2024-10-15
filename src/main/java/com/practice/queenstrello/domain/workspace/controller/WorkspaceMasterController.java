package com.practice.queenstrello.domain.workspace.controller;

import com.practice.queenstrello.domain.workspace.service.WorkspaceMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WorkspaceMasterController {

    private final WorkspaceMasterService workspaceMasterService;

    // 일반 유저가 MASTER 권한 변경 신청 API
    public ResponseEntity<String> saveMasterRequest(
            Long authUser
    ){
        return ResponseEntity.ok(workspaceMasterService.saveMasterRequest(authUser));
    }
}
