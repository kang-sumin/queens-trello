package com.practice.queenstrello.domain.common.aop;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.card.entity.Card;
import com.practice.queenstrello.domain.card.repository.CardRepository;
import com.practice.queenstrello.domain.comment.dto.response.CommentSaveResponse;
import com.practice.queenstrello.domain.comment.entity.Comment;
import com.practice.queenstrello.domain.comment.repository.CommentRepository;
import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.notify.service.SlackService;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.repository.UserRepository;
import com.practice.queenstrello.domain.workspace.dto.request.WorkspaceMemberEmailRequest;
import com.practice.queenstrello.domain.workspace.entity.Workspace;
import com.practice.queenstrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AspectPractice {

    private final SlackService slackService;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final CardRepository cardRepository;
    private final CommentRepository commentRepository;


    @Pointcut("@annotation(com.practice.queenstrello.domain.common.aop.annotation.SlackMaster)")
    private void slackMasterAnnotation() {}

    @Pointcut("@annotation(com.practice.queenstrello.domain.common.aop.annotation.SlackInvite)")
    private void slackInviteAnnotation() {}

    @Pointcut("@annotation(com.practice.queenstrello.domain.common.aop.annotation.SlackAddMember)")
    private void slackMemberAnnotation() {}

    @Pointcut("@annotation(com.practice.queenstrello.domain.common.aop.annotation.SlackCard)")
    private void slackCardAnnotation() {}

    @Pointcut("@annotation(com.practice.queenstrello.domain.common.aop.annotation.SlackComment)")
    private void slackCommentAnnotation() {}

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

    @Async
    @AfterReturning("slackMemberAnnotation()")
    public void slackMember(JoinPoint joinPoint) {
        try{
            Long workspaceId = (Long) joinPoint.getArgs()[0];
            WorkspaceMemberEmailRequest request = (WorkspaceMemberEmailRequest) joinPoint.getArgs()[2];
            User invited = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_USER));
            Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(()->new QueensTrelloException(ErrorCode.WORKSPACE_NOT_FOUND));
            List<User> memberList = workspace.getMembers().stream().map(wm->wm.getMember()).toList();
            for(User member : memberList) {
                if(!Objects.equals(member.getId(), invited.getId())) {
                    slackService.addMember(member.getId(), workspaceId, invited.getId());
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterReturning("slackCardAnnotation()")
    public void slackCard(JoinPoint joinPoint) {
        try {
            Long cardId = (Long) joinPoint.getArgs()[0];
            Long workspaceId = (Long) joinPoint.getArgs()[3];
            Long userId = (Long) joinPoint.getArgs()[2];

            Card card = cardRepository.findById(cardId).orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_CARD));
            List<User> managers = card.getCardManagers().stream().map(cm -> cm.getManager()).toList();
            for (User manager : managers) {
                //자신이 수정한 카드일경우 알림이 안옴
                if (!Objects.equals(manager.getId(), userId)) {
                    slackService.changeCard(manager.getId(), workspaceId, cardId);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Around("slackCommentAnnotation()")
    public Object slackComment(ProceedingJoinPoint joinPoint) throws Throwable {
        try{
            Long creatorId = (Long) joinPoint.getArgs()[2];
            CommentSaveResponse result = (CommentSaveResponse) joinPoint.proceed();

            Comment comment = commentRepository.findById(result.getCommentId()).orElseThrow(()-> new QueensTrelloException(ErrorCode.INVALID_COMMENT));
            Card card = comment.getCard();
            List<User> managers = card.getCardManagers().stream().map(cm -> cm.getManager()).toList();
            for (User manager : managers) {
                //자신이 수정한 카드일경우 알림이 안옴
                if (!Objects.equals(manager.getId(), creatorId)) {
                    slackService.addComment(manager.getId(),result.getCommentId());
                }
            }
            return result;
        } catch (Exception e) {
            throw e;
        }
    }



}
