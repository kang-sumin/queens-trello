package com.practice.queenstrello.domain.workspace.controller;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.workspace.dto.request.WorkspaceMemberRequest;
import com.practice.queenstrello.domain.workspace.dto.response.MasterRequestResponse;
import com.practice.queenstrello.domain.workspace.service.WorkspaceAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class WorkspaceAdminController {

    private final WorkspaceAdminService workspaceAdminService;

    // Master로 권한 변경
    @PatchMapping("/users/{userId}")
    public ResponseEntity<String> updateUserRole(
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(workspaceAdminService.updateUserRole(userId));
    }

    // 승인되지 않은 Master 권한 변경 요청 내역 조회 (다건 조회)
    @GetMapping("/master-request")
    public ResponseEntity<Page<MasterRequestResponse>> getMasterRequests(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(workspaceAdminService.getMasterRequests(page, size));
    }

    // Member 권한 변경
    @PatchMapping("workspace/{workspaceId}/member-users/{userId}")
    public ResponseEntity<String> updateMemberRole(
            @PathVariable("workspaceId") Long workspaceId,
            @PathVariable("userId") Long userId,
            @RequestBody WorkspaceMemberRequest workspaceMemberRequest
    ) {
        return ResponseEntity.ok(workspaceAdminService.updateMemberRole(workspaceId, userId, workspaceMemberRequest));
    }




}
