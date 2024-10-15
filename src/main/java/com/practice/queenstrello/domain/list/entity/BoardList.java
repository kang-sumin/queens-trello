package com.practice.queenstrello.domain.list.entity;

import com.practice.queenstrello.domain.board.entity.Board;
import com.practice.queenstrello.domain.card.entity.Card;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="lists")
public class BoardList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="list_id")
    private Long id;

    @Column(name="list_title", nullable=false, length=200)
    private String title;

    @Column(name="list_order", nullable=false)
    private Integer order;
    //Board와 다대일 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id", nullable=false)
    private Board board;
    //Card와 일대다 관계 설정
    @OneToMany(mappedBy = "boardList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();
    //생성자
    public BoardList(String title, Integer order, Board board) {
        this.title = title;
        this.order = order;
        this.board = board;
    }

    public BoardList(String title, Integer order) {
        this.title = title;
        this.order = order;
    }

    public void changeTitle(String title) {
        this.title = title;
    }
    //리스트 순서 수정 메서드
    public void changeOrder(Integer order) {
        this.order = order;
    }
    //리스트 카드 추가 메서드
    public void addCard(Card card) {
        cards.add(card);
        card.setBoardList(this); //양방향 연관관계 설정
    }
    //리스트 카드 제거 메서드
    public void removeCard(Card card) {
        cards.remove(card);
        card.setBoardList(null); //양방향 연관관계 해제
    }
}
