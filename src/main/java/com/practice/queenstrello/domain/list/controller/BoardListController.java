package com.practice.queenstrello.domain.list.controller;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.list.dto.request.BoardListSaveRequest;
import com.practice.queenstrello.domain.list.dto.request.BoardListUpdateRequest;
import com.practice.queenstrello.domain.list.dto.response.BoardListSaveResponse;
import com.practice.queenstrello.domain.list.entity.BoardList;
import com.practice.queenstrello.domain.list.service.BoardListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class BoardListController {

    private final BoardListService boardListService;

    @PostMapping("/boards/{boardId}/lists")
    public ResponseEntity<BoardListSaveResponse> savedBoardList (@RequestBody BoardListSaveRequest boardListSaveRequest, @AuthenticationPrincipal AuthUser authUser, @PathVariable("boardId") Long boardId) {
        BoardListSaveResponse boardListSaveResponse =  boardListService.savedBoardList(boardListSaveRequest, authUser, boardId);
        return ResponseEntity.ok(boardListSaveResponse);
    }

    @PutMapping("/boards/{boardId}/lists/{listId}")
    public ResponseEntity<BoardListSaveResponse> updateBoardList(
            @PathVariable Long boardListId,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody BoardListUpdateRequest boardListUpdateRequest,
            @PathVariable Long boardId
    ) {
        BoardListSaveResponse boardListResponse = boardListService.updateBoardList(boardListId, boardListUpdateRequest, authUser, boardId);
        return ResponseEntity.ok(boardListResponse);
    }

    @DeleteMapping("/boards/{boardId}/lists/{listId}")
    public void deleteBoardList(
            @PathVariable Long boardListId,
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long boardId
    ) {
        boardListService.deleteBoardList(boardListId, authUser, boardId);
    }
    //특정 위치에 새로운 보드리스트 삽입
    @PostMapping("/boards/{boardId}/lists/insert")
    public ResponseEntity<String> insertBoardListOrder(
            @PathVariable Long boardId,
            @RequestParam Integer targetOrder,
            @RequestBody BoardList boardList,
            @AuthenticationPrincipal AuthUser authUser
            ) {
        boardListService.insertBoardList(boardId, boardList, targetOrder);
        return ResponseEntity.ok("BoardList inserted successfully at order " + targetOrder + ".");
    }

    //기존 보드리스트의 순서변경
    @PutMapping("/boards/{boardId}/lists/insert/{listId}/order")
    public ResponseEntity<String> changeBoardListOrder(
            @PathVariable Long boardId,
            @PathVariable Long listId,
            @RequestParam Integer newOrder,
            @AuthenticationPrincipal AuthUser authUser) {

        boardListService.changeBoardListOrder(boardId, listId, newOrder);
        return ResponseEntity.ok("BoardList order changed successfully to " + newOrder + ".");
    }




}
