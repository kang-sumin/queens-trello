package com.practice.queenstrello.domain.common.aop;

import com.practice.queenstrello.domain.notify.service.SlackService;
import com.practice.queenstrello.domain.user.repository.UserRepository;
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
}
