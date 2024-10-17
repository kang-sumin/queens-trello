package com.practice.queenstrello.domain.workspace.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WorkspacePageController {
    @GetMapping("/workspace")
    public String createWorkspaces() {
        return "createWorkspace";
    }

    @GetMapping("/workspaces/{workspaceId}")
    public String moveWorkspace(@PathVariable Long workspaceId, Model model) {
        model.addAttribute("workspaceId", String.valueOf(workspaceId));
        return "workspace";
    }
}
