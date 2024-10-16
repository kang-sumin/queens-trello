package com.practice.queenstrello.domain.board.controller;

import com.practice.queenstrello.domain.board.dto.request.BoardSaveRequest;
import com.practice.queenstrello.domain.board.dto.request.BoardUpdateRequest;
import com.practice.queenstrello.domain.board.dto.response.BoardSaveResponse;
import com.practice.queenstrello.domain.board.service.BoardService;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/boards")
@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardSaveResponse> savedBoard(@RequestBody BoardSaveRequest boardSaveRequest, @RequestAttribute("user") User user) {
        BoardSaveResponse boardSaveResponse = boardService.savedBoard(boardSaveRequest, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(boardSaveResponse);

    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardSaveResponse> getBoard(@PathVariable long boardId) {
        BoardSaveResponse boardSaveResponse = boardService.getBoard(boardId);
        return ResponseEntity.ok(boardSaveResponse);
    }

    @GetMapping
    public ResponseEntity<List<BoardSaveResponse>> getBoards(
            @RequestParam Long workspaceId // 워크스페이스 ID를 요청 파라미터로 받음
    ) {
        List<BoardSaveResponse> boardResponseList = boardService.getBoards(workspaceId);
        return ResponseEntity.ok(boardResponseList);
    }


    @PutMapping("/{boardId}")
    public ResponseEntity<BoardSaveResponse> updateBoard(
            @PathVariable long boardId,
            @RequestBody BoardUpdateRequest boardUpdateRequest,
            @RequestAttribute("user") User user // 로그인한 사용자 정보
    ) {
        try {
            BoardSaveResponse boardResponse = boardService.updateBoard(boardId, boardUpdateRequest, user);
            return ResponseEntity.ok(boardResponse);
        } catch (QueensTrelloException Q) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }


    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @PathVariable long boardId,
            @RequestAttribute("user") User user // 로그인한 사용자 정보
    ) {
        try {
            boardService.deleteBoard(boardId, user);
            return ResponseEntity.noContent().build();
        } catch (QueensTrelloException Q) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}

