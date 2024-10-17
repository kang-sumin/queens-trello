package com.practice.queenstrello.domain.notify.controller;

import com.practice.queenstrello.domain.notify.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/slack")
@RequiredArgsConstructor
public class SlackController {
    private final SlackService slackService;


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
    @PostMapping("/invite/{inviterId}/{invitedId}/{workspaceId}")
    public String invite(@PathVariable Long inviterId, @PathVariable Long invitedId, @PathVariable Long workspaceId) {
        try {
            slackService.inviteMember(inviterId, invitedId,workspaceId);
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

    /**
     * 멤버 추가 알림
     * @param userId 알림을 받을 유저 ID
     * @param memberId 추가된 멤버의 ID
     * @return 성공 여부
     */
    @PostMapping("/member/{userId}/{workspaceId}/{memberId}")
    public String addMember(@PathVariable Long userId, @PathVariable Long workspaceId, @PathVariable Long memberId) {
        try {
            slackService.addMember(userId,workspaceId, memberId);
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

    /**
     *
     * @param userId 알림을 받을 유저 ID
     * @param cardId 변경된 카드 ID
     * @return 성공 여부
     */
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

    /**
     *
     * @param userId 알림을 받을 유저 ID
     * @param commentId 생성된 댓글 ID
     * @return 성공 여부
     */
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