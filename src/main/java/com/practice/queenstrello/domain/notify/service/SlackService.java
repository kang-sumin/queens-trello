package com.practice.queenstrello.domain.notify.service;

import com.practice.queenstrello.config.Color;
import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.repository.UserRepository;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
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
    public void sendMessage(AuthUser authUser, String title, String message) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(()->new NullPointerException("해당하는 아이디의 유저가 없습니다."));
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

    /**
     * Slack Field 생성
     **/
    private Field generateSlackField(String title, String value) {
        return Field.builder()
                .title(title)
                .value(value)
                .valueShortEnough(false)
                .build();
    }
}
