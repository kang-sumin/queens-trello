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
    //name 부분 삭제 필요 다른 분들이랑 맞출것
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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<BoardList> boardLists = new ArrayList<>();

    public Board(BoardSaveRequest boardSaveRequest, Workspace workspace) {
        this.title = boardSaveRequest.getTitle();
        this.backgroundColor = boardSaveRequest.getBackgroundColor();
        this.imageUrl = boardSaveRequest.getImageUrl();
        this.workspace = workspace;
    }

    public Board(Long id, String title, String backgroundColor, String imageUrl, Workspace workspace) {
        this.id = id;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.imageUrl = imageUrl;
        this.workspace = workspace;
    }

    public Board(String title, String backgroundColor, String imageUrl, Workspace workspace) {
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.imageUrl = imageUrl;
        this.workspace = workspace;
    }

    public void changeTitle(String title) {
        if (title != null && !title.isEmpty()) {
            this.title = title;
        }
    }

    public void changeBackgroundColor(String backgroundColor) {
        if (backgroundColor != null && !backgroundColor.isEmpty()) {
            this.backgroundColor = backgroundColor;
        }
    }

    public void changeImageUrl(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            this.imageUrl = imageUrl;
        }
    }

    public void updateBoard(String title, String backgroundColor, String imageUrl) {
        changeTitle(title);
        changeBackgroundColor(backgroundColor);
        changeImageUrl(imageUrl);
    }

    public void addBoardList(BoardList boardList) {
        boardLists.add(boardList);
        boardList.setBoard(this);
    }

    public void removeBoardList(BoardList boardList) {
        boardLists.remove(boardList);
        boardList.setBoard(null);
    }

}
