package com.practice.queenstrello.domain.notify.controller;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.notify.dto.request.SlackMessageRequest;
import com.practice.queenstrello.domain.notify.service.SlackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/slack")
@RequiredArgsConstructor
public class SlackController {
    private final SlackService slackService;

    @PostMapping("/send")
    public String sendMessage(@AuthenticationPrincipal AuthUser authUser, @RequestBody @Valid SlackMessageRequest request) {
        try {
            slackService.sendMessage(authUser,request.getTitle(), request.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }

        return "success";
    }
}