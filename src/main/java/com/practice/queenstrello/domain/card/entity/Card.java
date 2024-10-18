package com.practice.queenstrello.domain.card.entity;

import com.practice.queenstrello.domain.comment.entity.Comment;
import com.practice.queenstrello.domain.common.entity.CreatedTimestamped;
import com.practice.queenstrello.domain.list.entity.BoardList;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DialectOverride;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="cards")
public class Card extends CreatedTimestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="card_id")
    private Long id;

    @Column(name="card_title", nullable=false, length=100)
    private String title;

    @Column(name="card_content", nullable=false, length=500)
    private String content;

    @Column(name="card_deadline", nullable=false)
    private LocalDateTime deadLine;

    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "card",cascade = CascadeType.REMOVE)
    private List<CardManager> cardManagers = new ArrayList<>();

    @OneToMany(mappedBy = "card",cascade = CascadeType.REMOVE)
    private List<CardAttachments> cardAttachments = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="list_id", nullable = false)
    private BoardList boardList;

    @Version
    private Long version; //낙관적 락을 위한 버전 필드

    //카드 생성자
    public Card(String title, String content, LocalDateTime deadLine,BoardList boardList){
        this.title=title;
        this.content=content;
        this.deadLine=deadLine;
        this.boardList=boardList; //리스트와 연결
    }

    //담당자 추가 메서드
    public void addCardManager(CardManager cardManager){
        this.cardManagers.add(cardManager);
    }

    //카드 수정 메서드
    public void updateCard(String title, String content, LocalDateTime deadLine){
        this.title=title;
        this.content=content;
        this.deadLine=deadLine;
    }

    //public void addCardAttachments(CardAttachments cardAttachments){
        //this.cardAttachments.add(cardAttachments);
 //   }

}