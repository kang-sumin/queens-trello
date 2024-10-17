package com.practice.queenstrello.domain.card.entity;

import com.practice.queenstrello.domain.comment.entity.Comment;
import com.practice.queenstrello.domain.list.entity.BoardList;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="cards")
public class Card {

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

    @OneToMany(mappedBy = "card")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "card")
    private List<CardManager> cardManagers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="list_id", nullable = false)
    private BoardList boardList;

    @ElementCollection
    private List<String> fileUrls = new ArrayList<>(); // 여러 파일 URL을 저장
}
