package com.practice.queenstrello.domain.list.entity;

import com.practice.queenstrello.domain.board.entity.Board;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="borad_id", nullable=false)
    private Board board;
}
