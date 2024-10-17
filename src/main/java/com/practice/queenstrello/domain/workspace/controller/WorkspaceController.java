package com.practice.queenstrello.domain.workspace.controller;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.workspace.dto.request.WorkspaceMemberEmailRequest;
import com.practice.queenstrello.domain.workspace.dto.request.WorkspaceRequest;
import com.practice.queenstrello.domain.workspace.dto.response.WorkspaceResponse;
import com.practice.queenstrello.domain.workspace.dto.response.WorkspaceUpdateResponse;
import com.practice.queenstrello.domain.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    // 워크 스페이스 생성
    @PostMapping("/workspace")
    public ResponseEntity<WorkspaceResponse> saveWorkspace(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody WorkspaceRequest workspaceRequest
    ) {
        return ResponseEntity.ok(workspaceService.saveWorkspace(authUser, workspaceRequest));
    }

    // 워크 스페이스에 멤버 초대
    @PostMapping("/workspace/{workspaceId}/member")
    public ResponseEntity<String> addMember(
            @PathVariable("workspaceId") Long workspaceId,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody WorkspaceMemberEmailRequest workspaceMemberEmailRequest
    ) {
        return ResponseEntity.ok(workspaceService.addMember(workspaceId, authUser, workspaceMemberEmailRequest));
    }

    // 워크 스페이스 수정
    @PatchMapping("/workspace/{workspaceId}")
    public ResponseEntity<WorkspaceUpdateResponse> updateWorkspace(
            @PathVariable("workspaceId") Long workspaceId,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody WorkspaceRequest workspaceRequest
    ) {
        return ResponseEntity.ok(workspaceService.updateWorkspace(workspaceId, authUser, workspaceRequest));
    }

    // 워크스페이스 조회 (유저가 멤버로 가입된 자신의 워크스페이스 목록을 확인할 수 있음)
    @GetMapping("/workspace")
    public ResponseEntity<Page<WorkspaceResponse>> getUserWorkspace(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal AuthUser authUser
    ){
        return ResponseEntity.ok(workspaceService.getUserWorkspace(page, size, authUser));
    }

    // 워크 스페이스 삭제
    @DeleteMapping("/workspace/{workspaceId}")
    public ResponseEntity<String> deleteWorkspace(
            @PathVariable("workspaceId") Long workspaceId,
            @AuthenticationPrincipal AuthUser authUser
    ){
        return ResponseEntity.ok(workspaceService.deleteWorkspace(workspaceId, authUser));
    }

}
