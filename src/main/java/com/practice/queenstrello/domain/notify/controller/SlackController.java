package com.practice.queenstrello.domain.notify.controller;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.notify.dto.request.SlackMessageRequest;
import com.practice.queenstrello.domain.notify.service.SlackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/slack")
@RequiredArgsConstructor
public class SlackController {
    private final SlackService slackService;

    /**
     * 기본 메세지 전송 test
     * @param authUser : 로그인된 회원정보
     * @param request : 메세지 내용
     * @return
     */

    @PostMapping("/send")
    public String sendMessage(@AuthenticationPrincipal AuthUser authUser, @RequestBody @Valid SlackMessageRequest request) {
        try {
            slackService.sendMessage(authUser.getUserId(),request.getTitle(), request.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

    /**
     * 마스터 권한 힉득 메세지
     * @param userId
     * @return
     */
    @PostMapping("/master/{userId}")
    public String upgradeMaster(@PathVariable Long userId) {
        try {
            slackService.upgradeMaster(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }
}