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
@Table(name="card_attachments")
public class CardAttachments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="card_attachment_id")
    private Long id;

    @Column(name="file_url", nullable=false)
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="card_id", nullable=false)
    private Card card;

    public CardAttachments(String fileUrl, Card card) {
        this.fileUrl = fileUrl;
        this.card = card;
    }
}


