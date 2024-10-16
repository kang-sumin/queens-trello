package com.practice.queenstrello.domain.card.entity;

import com.practice.queenstrello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name="card_log")
public class CardLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String log; //수정 내용에 대한 설명
    private LocalDateTime createdAt; //로그 생성 시간

    @ManyToOne(fetch = FetchType.LAZY) //수정된 사용자 정보
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) //수정된 카드 정보
    @JoinColumn(name="card_id")
    private Card card;

    // 생성자 : 로그 생성에 필요한 필드들
    public CardLog(User user, Card card, String log){
        this.user=user;
        this.card=card;
        this.log=log;
        this.createdAt=LocalDateTime.now(); //생성시간 자동 설정
    }
}
