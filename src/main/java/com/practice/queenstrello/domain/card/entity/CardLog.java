package com.practice.queenstrello.domain.card.entity;

import com.practice.queenstrello.domain.common.entity.CreatedTimestamped;
import com.practice.queenstrello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name="card_log")
public class CardLog extends CreatedTimestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String log; //수정 내용에 대한 설명


    //수정된 사용자 정보
    @Column(name="user_id")
    private Long userId;

    //수정된 카드 정보
    @Column(name="card_id")
    private Long cardId;

    // 생성자 : 로그 생성에 필요한 필드들
    public CardLog(Long user, Long card, String log){
        this.userId=user;
        this.cardId=card;
        this.log=log;
    }
}
