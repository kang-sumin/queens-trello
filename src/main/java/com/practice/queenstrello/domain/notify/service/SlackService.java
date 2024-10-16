package com.practice.queenstrello.domain.notify.service;

import com.practice.queenstrello.config.Color;
import com.practice.queenstrello.domain.notify.entity.Classification;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.repository.UserRepository;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.slack.api.webhook.WebhookPayloads.payload;

@Service
@RequiredArgsConstructor
public class SlackService {

    private final UserRepository userRepository;

    private final Slack slackClient = Slack.getInstance();

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
        String createUrl = "https://www.notion.so/teamsparta/Code-Queens-515f826af1cb45a7868f487fa02fe271";

        User user = userRepository.findById(userId).orElseThrow(()->new NullPointerException("해당하는 아이디의 유저가 없습니다."));
        String slackUrl = user.getSlackUrl();
        try {
            slackClient.send(slackUrl, payload(p -> p
                    .text(title) // 메시지 제목
                    .iconUrl("https://raw.githubusercontent.com/kang-sumin/queens-trello/refs/heads/feat/search/src/main/resources/static/img/queens-icon.webp")
                    .username("queens-trello")
                    .attachments(List.of(
                            Attachment.builder()
                                    .color(Color.GREEN.getCode()) // 메시지 색상
                                    .serviceUrl(createUrl)  //워크스페이스 생성 url
                                    .pretext(message)// 메시지 본문 내용
                                    .text("<"+createUrl+"|"+"워크스페이스 생성하기"+">")
                                    .build())))

            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
