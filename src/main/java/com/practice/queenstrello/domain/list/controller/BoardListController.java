package com.practice.queenstrello.domain.list.controller;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.list.dto.request.BoardListSaveRequest;
import com.practice.queenstrello.domain.list.dto.request.BoardListUpdateRequest;
import com.practice.queenstrello.domain.list.dto.response.BoardListSaveResponse;
import com.practice.queenstrello.domain.list.service.BoardListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/{boardId}/lists")
@RestController
@RequiredArgsConstructor
public class BoardListController {
    private final BoardListService boardListService;

    @PostMapping
    public ResponseEntity<BoardListSaveResponse> savedBoardList (@RequestBody BoardListSaveRequest boardListSaveRequest, @AuthenticationPrincipal AuthUser authUser, @PathVariable("boardId") Long boardId) {
        BoardListSaveResponse boardListSaveResponse =  boardListService.savedBoardList(boardListSaveRequest, authUser, boardId);
        return ResponseEntity.ok(boardListSaveResponse);
    }

    @PutMapping("/{listId}")
    public ResponseEntity<BoardListSaveResponse> updateBoardList(
            @PathVariable long boardListId,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody BoardListUpdateRequest boardListUpdateRequest,
            @PathVariable long boardId
    ) {
        BoardListSaveResponse boardListResponse = boardListService.updateBoardList(boardListId, boardListUpdateRequest, authUser, boardId);
        return ResponseEntity.ok(boardListResponse);
    }

    @DeleteMapping("/{listId}")
    public void deleteBoardList(
            @PathVariable long boardListId,
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable long boardId
    ) {
        boardListService.deleteBoardList(boardListId, authUser, boardId);
    }


}
