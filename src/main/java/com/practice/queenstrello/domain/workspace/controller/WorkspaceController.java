package com.practice.queenstrello.domain.workspace.controller;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.workspace.dto.request.WorkspaceSaveRequest;
import com.practice.queenstrello.domain.workspace.dto.response.WorkspaceResponse;
import com.practice.queenstrello.domain.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    // 워크 스페이스 생성
    @PostMapping("/workspace")
    public ResponseEntity<WorkspaceResponse> saveWorkspace(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody WorkspaceSaveRequest workspaceSaveRequest
    ){
        return ResponseEntity.ok(workspaceService.saveWorkspace(authUser, workspaceSaveRequest));
    }

}
