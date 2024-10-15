package com.practice.queenstrello.domain.list.controller;

import com.practice.queenstrello.domain.board.dto.request.BoardSaveRequest;
import com.practice.queenstrello.domain.board.dto.request.BoardUpdateRequest;
import com.practice.queenstrello.domain.board.dto.response.BoardSaveResponse;
import com.practice.queenstrello.domain.board.entity.Board;
import com.practice.queenstrello.domain.list.dto.request.BoardListSaveRequest;
import com.practice.queenstrello.domain.list.dto.request.BoardListUpdateRequest;
import com.practice.queenstrello.domain.list.dto.response.BoardListSaveResponse;
import com.practice.queenstrello.domain.list.entity.BoardList;
import com.practice.queenstrello.domain.list.service.BoardListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/lists")
@RestController
@RequiredArgsConstructor
public class BoardListController {
    private final BoardListService boardListService;

    @PostMapping
    public ResponseEntity<BoardListSaveResponse> savedBoardList (@RequestBody BoardListSaveRequest boardListSaveRequest) {
        BoardListSaveResponse boardListSaveResponse =  boardListService.savedBoardList(boardListSaveRequest);
        return ResponseEntity.ok(boardListSaveResponse);
    }

    @PutMapping("/{listId}")
    public ResponseEntity<BoardListSaveResponse> updateBoardList(
            @PathVariable long boardListId,
            @RequestBody BoardListUpdateRequest boardListUpdateRequest
    ) {
        BoardListSaveResponse boardListResponse = boardListService.updateBoardList(boardListId, boardListUpdateRequest);
        return ResponseEntity.ok(boardListResponse);
    }

    @DeleteMapping("/{listId}")
    public void deleteBoardList(
            @PathVariable long boardListId
    ) {
        boardListService.deleteBoardList(boardListId);
    }


}
