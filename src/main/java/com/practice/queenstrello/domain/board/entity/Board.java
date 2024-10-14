package com.practice.queenstrello.domain.board.entity;

import com.practice.queenstrello.domain.common.entity.ModifiedTimestamped;
import com.practice.queenstrello.domain.list.entity.BoardList;
import com.practice.queenstrello.domain.workspace.entity.Workspace;
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
@Table(name="boards")
public class Board extends ModifiedTimestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="board_id")
    private Long id;

    @Column(name="board_title", nullable=false, length=200)
    private String title;

    @Column(name="board_background", length=200)
    private String backgroundColor;

    @Column(name="board_image", length=200)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="workspace_id", nullable=false)
    private Workspace workspace;

    @OneToMany(mappedBy = "board")
    private List<BoardList> boardLists = new ArrayList<>();


}
