package com.practice.queenstrello.domain.board.entity;

import com.practice.queenstrello.domain.board.dto.request.BoardSaveRequest;
import com.practice.queenstrello.domain.comment.entity.Comment;
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

    @Column(name="board_image_url", length=200)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="workspace_id", nullable=false)
    private Workspace workspace;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<BoardList> boardLists = new ArrayList<>();

    public Board(BoardSaveRequest boardSaveRequest) {
        this.id = boardSaveRequest.getId();
        this.title = boardSaveRequest.getTitle();
        this.backgroundColor = boardSaveRequest.getBackgroundColor();
        this.imageUrl = boardSaveRequest.getImageUrl();
    }

    public Board(Long id, String title, String backgroundColor, String imageUrl) {
        this.id = id;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.imageUrl = imageUrl;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void changeImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
