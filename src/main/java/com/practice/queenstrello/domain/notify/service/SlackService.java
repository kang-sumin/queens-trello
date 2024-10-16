package com.practice.queenstrello.domain.notify.service;

import com.practice.queenstrello.config.Color;
import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.notify.entity.Classification;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.repository.UserRepository;
import com.practice.queenstrello.domain.workspace.entity.Workspace;
import com.practice.queenstrello.domain.workspace.repository.WorkspaceRepository;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
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

    private final Slack slackClient = Slack.getInstance();

//    private String uriAddress = protocol+"://" + ip + ":" + port + "/";

    /**
     * 슬랙 메시지 전송
     **/
    public void sendMessage(Long userId, String title, String message) {
        User user = userRepository.findById(userId).orElseThrow(()->new NullPointerException("해당하는 아이디의 유저가 없습니다."));
        String slackUrl = user.getSlackUrl();
        try {
            slackClient.send(slackUrl, payload(p -> p
                    .text(title) // 메시지 제목
                    .iconUrl("https://raw.githubusercontent.com/kang-sumin/queens-trello/refs/heads/feat/search/src/main/resources/static/img/queens-icon.webp")
                    .username("queens-trello")
                    .attachments(List.of(
                            Attachment.builder().color(Color.GREEN.getCode()) // 메시지 색상
                                    .text(message)            // 메시지 본문 내용
                                    .build())))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void upgradeMaster(Long userId) {
        Classification classification = Classification.Master;
        String title = classification.getTitle();
        String message = "당신은 마스터로 승급되었습니다. 당신만의 워크스페이스를 만들어보세요!";
        String createUrl = combineAddress()+"/workspace";

        User user = userRepository.findById(userId).orElseThrow(()->new QueensTrelloException(ErrorCode.INVALID_USER));
        String slackUrl = user.getSlackUrl();
        try {
            slackClient.send(slackUrl, payload(p -> p
                    .text(title) // 메시지 제목
                    .iconUrl("https://raw.githubusercontent.com/kang-sumin/queens-trello/refs/heads/feat/search/src/main/resources/static/img/queens-icon.webp")
                    .username("queens-trello")
                    .attachments(List.of(
                            Attachment.builder()
                                    .color(Color.GREEN.getCode()) // 메시지 색상
                                    .pretext(message)// 메시지 본문 내용
                                    .text("<"+createUrl+"|"+"워크스페이스 생성하기"+">")  //워크스페이스 생성 url
                                    .build())))

            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void inviteMember(Long inviterId, Long invitedId) {
        User inviter = userRepository.findById(inviterId).orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_USER));
        User invited = userRepository.findById(invitedId).orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_USER));
        String slackUrl = invited.getSlackUrl();
        Classification classification = Classification.Invite;
        String title = inviter.getNickname() + classification.getTitle();
        Workspace workspace = workspaceRepository.findByMasterUser(inviter).orElseThrow(()-> new QueensTrelloException(ErrorCode.No_WORKSPACE_MASTER));
        String moveUrl = combineAddress()+"/workspaces/"+workspace.getId();
        String message = inviter.getNickname()+"님의 작업공간 "+workspace.getName() +" 입니다! \n 우리 함께 끝까지 달려봐요!";

        try {
            slackClient.send(slackUrl, payload(p -> p
                    .text(title) // 메시지 제목
                    .iconUrl("https://raw.githubusercontent.com/kang-sumin/queens-trello/refs/heads/feat/search/src/main/resources/static/img/queens-icon.webp")
                    .username("queens-trello")
                    .attachments(List.of(
                            Attachment.builder()
                                    .color(Color.GREEN.getCode()) // 메시지 색상
                                    .pretext(message)// 메시지 본문 내용
                                    .text("<"+moveUrl+"|"+workspace.getName()+"> \n" +workspace.getDescription())

                                    .build())))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String combineAddress() {
        return protocol+"://" + ip + ":" + port;
    }

}
