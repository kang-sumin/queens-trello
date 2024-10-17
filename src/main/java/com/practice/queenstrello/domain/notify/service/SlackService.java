package com.practice.queenstrello.domain.notify.service;

import com.practice.queenstrello.config.Color;
import com.practice.queenstrello.domain.card.entity.Card;
import com.practice.queenstrello.domain.card.repository.CardRepository;
import com.practice.queenstrello.domain.comment.entity.Comment;
import com.practice.queenstrello.domain.comment.repository.CommentRepository;
import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.notify.entity.Classification;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.repository.UserRepository;
import com.practice.queenstrello.domain.workspace.entity.Workspace;
import com.practice.queenstrello.domain.workspace.repository.WorkspaceRepository;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.slack.api.webhook.WebhookPayloads.payload;

@Service
@RequiredArgsConstructor
public class SlackService {

    @Value("${http.protocol}")
    private String protocol;

    @Value("${http.ip}")
    private String ip;

    @Value("${server.port}")
    private String port;

    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final CardRepository cardRepository;
    private final CommentRepository commentRepository;

    private final Slack slackClient = Slack.getInstance();


    /**
     * 슬랙 메시지 전송 메서드
     **/
    public void sendMessage(Classification classification, String slackUrl, String title, String message, String content, String fieldTitle, String fieldContent) {
        try {
            //알림 안에 링크가 들어간 경우
            if(classification.equals(Classification.Master)||classification.equals(Classification.Invite)) {
                slackClient.send(slackUrl, payload(p -> p
                        .text(title) // 메시지 제목
                        .iconUrl("https://raw.githubusercontent.com/kang-sumin/queens-trello/refs/heads/feat/search/src/main/resources/static/img/queens-icon.webp")
                        .username("queens-trello")
                        .attachments(List.of(
                                Attachment.builder()
                                        .color(Color.GREEN.getCode()) // 메시지 색상
                                        .pretext(message)// 메시지 본문 내용
                                        .text(content)  //이동할 url
                                        .build())))

                );
                //자세한 내용이 있는 경우
            } else if(classification.equals(Classification.Card)||classification.equals(Classification.Comment)) {
                slackClient.send(slackUrl, payload(p -> p
                        .text(title) // 메시지 제목
                        .iconUrl("https://raw.githubusercontent.com/kang-sumin/queens-trello/refs/heads/feat/search/src/main/resources/static/img/queens-icon.webp")
                        .username("queens-trello")
                        .attachments(List.of(
                                Attachment.builder()
                                        .color(Color.GREEN.getCode()) // 메시지 색상
                                        .pretext(message)// 메시지 본문 내용
                                        .fields(List.of(
                                                new Field(fieldTitle, fieldContent, false)
                                        ))
                                        .build())))
                );
                //알림 내용이 한줄인 경우
            } else {
                slackClient.send(slackUrl, payload(p -> p
                        .text(title) // 메시지 제목
                        .iconUrl("https://raw.githubusercontent.com/kang-sumin/queens-trello/refs/heads/feat/search/src/main/resources/static/img/queens-icon.webp")
                        .username("queens-trello")
                        .attachments(List.of(
                                Attachment.builder()
                                        .color(Color.GREEN.getCode()) // 메시지 색상
                                        .pretext(message)// 메시지 본문 내용
                                        .build())))
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void upgradeMaster(Long userId) {
        Classification classification = Classification.Master;
        String title = classification.getTitle();
        String message = "당신은 마스터로 승급되었습니다. 당신만의 워크스페이스를 만들어보세요!";
        String createUrl = combineAddress()+"/workspace";
        String content = "<"+createUrl+"|"+"워크스페이스 생성하기"+">";

        User receiver = findUser(userId);
        String slackUrl = receiver.getSlackUrl();
        sendMessage(classification, slackUrl, title, message, content,null,null);
    }

    public void inviteMember(Long inviterId, Long invitedId) {
        User inviter = findUser(inviterId);
        User invited = findUser(invitedId);
        String slackUrl = invited.getSlackUrl();
        Classification classification = Classification.Invite;
        String title = inviter.getNickname() + classification.getTitle();
        Workspace workspace = workspaceRepository.findByMasterUser(inviter).orElseThrow(()-> new QueensTrelloException(ErrorCode.No_WORKSPACE_MASTER));
        String moveUrl = combineAddress()+"/workspaces/"+workspace.getId();
        String content = "<"+moveUrl+"|"+workspace.getName()+"> \n" +workspace.getDescription();
        String message = inviter.getNickname()+"님의 작업공간 "+workspace.getName() +" 입니다! \n 우리 함께 끝까지 달려봐요!";
        sendMessage(classification, slackUrl, title, message, content,null,null);
    }


    public void addMember(Long userId, Long memberId) {
        User receiver = findUser(userId);
        String slackUrl = receiver.getSlackUrl();
        Classification classification = Classification.Member;
        String title = classification.getTitle();
        User member = findUser(memberId);
        String message = "우리 워크스페이스에 "+ member.getNickname()+"님이 오셨어요! 어서 와서 환영해주세요!";
        sendMessage(classification, slackUrl, title, message,null,null,null);
    }

    public void changeCard(Long userId, Long cardId) {
        User receiver = findUser(userId);
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_CARD));
        //card의 매니저인지 확인
        checkManager(card, receiver);
        String slackUrl = receiver.getSlackUrl();
        Classification classification = Classification.Card;
        String title = classification.getTitle();
        String message = card.getTitle()+" 카드 내용을 꼼꼼히 확인하세요!";
        sendMessage(classification, slackUrl, title, message,null,card.getTitle(),card.getContent());
    }

    public void addComment(Long userId, Long commentId) {
        User receiver = findUser(userId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new QueensTrelloException(ErrorCode.INVALID_COMMENT));
        Card card = comment.getCard();
        //card의 매니저인지 확인
        checkManager(card, receiver);
        String slackUrl = receiver.getSlackUrl();
        Classification classification = Classification.Comment;
        String title = classification.getTitle();
        String message = card.getTitle() +"카드에 "+comment.getUser().getNickname()+"님이 작성한 댓글입니다.";
        sendMessage(classification, slackUrl, title, message,null,"From."+comment.getUser().getNickname(),comment.getContent());
    }

    private String combineAddress() {
        return protocol+"://" + ip + ":" + port;
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_USER));
    }

    private void checkManager(Card card, User user) {
        if(card.getCardManagers().stream().filter(cardManager -> cardManager.getManager()==user).toList().isEmpty()) throw new QueensTrelloException(ErrorCode.NOT_CARD_MANAGER);
    }
}
