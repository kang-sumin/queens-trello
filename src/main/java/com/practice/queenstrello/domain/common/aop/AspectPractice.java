package com.practice.queenstrello.domain.common.aop;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.notify.service.SlackService;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.repository.UserRepository;
import com.practice.queenstrello.domain.workspace.dto.request.WorkspaceMemberEmailRequest;
import com.practice.queenstrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AspectPractice {

    private final SlackService slackService;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;


    @Pointcut("@annotation(com.practice.queenstrello.domain.notify.annotation.SlackMaster)")
    private void slackMasterAnnotation() {}

    @Pointcut("@annotation(com.practice.queenstrello.domain.notify.annotation.SlackInvite)")
    private void slackInviteAnnotation() {}

    //마스터 승급시 알림
    @AfterReturning("slackMasterAnnotation()")
    public void slackMaster(JoinPoint joinPoint) {
        try{
            Long userId = (Long) joinPoint.getArgs()[0];
            slackService.upgradeMaster(userId);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    //초대받은 사람에게 알림
    @AfterReturning("slackInviteAnnotation()")
    public void slackInvite(JoinPoint joinPoint) {
        try {
            Long workspaceId = (Long) joinPoint.getArgs()[0];
            AuthUser inviter = (AuthUser) joinPoint.getArgs()[1];
            WorkspaceMemberEmailRequest request = (WorkspaceMemberEmailRequest) joinPoint.getArgs()[2];
            User invited = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_USER));
            slackService.inviteMember(inviter.getUserId(), invited.getId(), workspaceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
