package com.practice.queenstrello.domain.board.controller;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.board.dto.request.BoardSaveRequest;
import com.practice.queenstrello.domain.board.dto.request.BoardUpdateRequest;
import com.practice.queenstrello.domain.board.dto.response.BoardSaveResponse;
import com.practice.queenstrello.domain.board.service.BoardService;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/{workspaceId}/boards")
@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardSaveResponse> savedBoard(@PathVariable("workspaceId") Long workspaceId, @RequestBody BoardSaveRequest boardSaveRequest, @AuthenticationPrincipal AuthUser authUser) {
        BoardSaveResponse boardSaveResponse = boardService.savedBoard(workspaceId, boardSaveRequest, authUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(boardSaveResponse);

    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardSaveResponse> getBoard(@PathVariable long boardId, @AuthenticationPrincipal AuthUser authUser) {
        BoardSaveResponse boardSaveResponse = boardService.getBoard(boardId, authUser);
        return ResponseEntity.ok(boardSaveResponse);
    }

    @GetMapping
    public ResponseEntity<List<BoardSaveResponse>> getBoards(
            @PathVariable Long workspaceId,// 워크스페이스 ID를 요청 파라미터로 받음
            @AuthenticationPrincipal AuthUser authUser
    ) {
        List<BoardSaveResponse> boardResponseList = boardService.getBoards(workspaceId, authUser);
        return ResponseEntity.ok(boardResponseList);
    }


    @PutMapping("/{boardId}")
    public ResponseEntity<BoardSaveResponse> updateBoard(
            @PathVariable long boardId,
            @RequestBody BoardUpdateRequest boardUpdateRequest,
            @AuthenticationPrincipal AuthUser authUser,// 로그인한 사용자 정보
            @PathVariable("workspaceId") Long workspaceId
    ) {
        try {
            BoardSaveResponse boardResponse = boardService.updateBoard(boardId, boardUpdateRequest, authUser, workspaceId);
            return ResponseEntity.ok(boardResponse);
        } catch (QueensTrelloException Q) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }


    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @PathVariable long boardId,// 로그인한 사용자 정보
            @AuthenticationPrincipal AuthUser authUser
    ) {
        try {
            boardService.deleteBoard(boardId, authUser);
            return ResponseEntity.noContent().build();
        } catch (QueensTrelloException Q) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}

