package com.practice.queenstrello.domain.card.entity;

import com.practice.queenstrello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="card_managers")
public class CardManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="card_manager_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="card_id", nullable=false)
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="manager_id", nullable = false)
    private User manager;

    //카드 담당자 생성자
    public CardManager(Card card, User manager){
        this.card=card;
        this.manager=manager;
    }

}
