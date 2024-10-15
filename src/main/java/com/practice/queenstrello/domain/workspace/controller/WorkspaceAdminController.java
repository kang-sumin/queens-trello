package com.practice.queenstrello.domain.workspace.controller;

import com.practice.queenstrello.domain.workspace.dto.response.MasterRequestResponse;
import com.practice.queenstrello.domain.workspace.service.WorkspaceAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class WorkspaceAdminController {

    private final WorkspaceAdminService workspaceAdminService;

    // 승인되지 않은 Master 권한 변경 요청 내역 조회 (다건 조회)
    @GetMapping("/master-request")
    public ResponseEntity<Page<MasterRequestResponse>> getMasterRequests(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            AuthUser authUser
    ){
        return ResponseEntity.ok(workspaceAdminService.getMasterRequests(page, size, authUser));
    }

}
