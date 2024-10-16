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
     * @param request  : 메세지 내용
     * @return  성공여부
     */

    @PostMapping("/send")
    public String sendMessage(@AuthenticationPrincipal AuthUser authUser, @RequestBody @Valid SlackMessageRequest request) {
        try {
            slackService.sendMessage(authUser.getUserId(), request.getTitle(), request.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

    /**
     * 마스터 권한 힉득 메세지
     *
     * @param userId : 알림을 받을 유저 ID
     * @return 성공여부
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

    /**
     * @param inviterId 초대한 유저ID
     * @param invitedId 초대받은 유저 ID
     * @return 성공 여부
     */
    @PostMapping("/invite/{inviterId}/{invitedId}")
    public String invite(@PathVariable Long inviterId, @PathVariable Long invitedId) {
        try {
            slackService.inviteMember(inviterId, invitedId);
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

    @PostMapping("/member/{userId}/{memberId}")
    public String addMember(@PathVariable Long userId, @PathVariable Long memberId) {
        try {
            slackService.addMember(userId, memberId);
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

    @PostMapping("/card/{userId}/{cardId}")
    public String changeCard(@PathVariable Long userId, @PathVariable Long cardId) {
        try {
            slackService.changeCard(userId, cardId);
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }
    @PostMapping("/comment/{userId}/{commentId}")
    public String addComment(@PathVariable Long userId, @PathVariable Long commentId) {
        try {
            slackService.addComment(userId, commentId);
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

}