package com.practice.queenstrello.domain.board.controller;

import com.practice.queenstrello.domain.board.dto.request.BoardSaveRequest;
import com.practice.queenstrello.domain.board.dto.request.BoardUpdateRequest;
import com.practice.queenstrello.domain.board.dto.response.BoardSaveResponse;
import com.practice.queenstrello.domain.board.dto.response.BoardUpdateResponse;
import com.practice.queenstrello.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/boards")
@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardSaveResponse> savedBoard(@RequestBody BoardSaveRequest boardSaveRequest) {
        BoardSaveResponse boardSaveResponse =  boardService.savedBoard(boardSaveRequest);
        return ResponseEntity.ok(boardSaveResponse);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardSaveResponse> getBoard(@PathVariable long boardId) {
        BoardSaveResponse boardSaveResponse = boardService.getBoard(boardId);
        return ResponseEntity.ok(boardSaveResponse);
    }

    @GetMapping
    public ResponseEntity<List<BoardSaveResponse>> getBoards() {
        List<BoardSaveResponse> boardResponseList = boardService.getBoards();
        return ResponseEntity.ok(boardResponseList);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<BoardSaveResponse> updateBoard(
            @PathVariable long boardId,
            @RequestBody BoardUpdateRequest boardUpdateRequest
    ) {
        BoardSaveResponse boardResponse = boardService.updateBoard(boardId, boardUpdateRequest);
        return ResponseEntity.ok(boardResponse);
    }

    @DeleteMapping("/{boardId}")
    public void deleteBoard(
            @PathVariable long boardId
    ) {
        boardService.deleteBoard(boardId);
    }
}

