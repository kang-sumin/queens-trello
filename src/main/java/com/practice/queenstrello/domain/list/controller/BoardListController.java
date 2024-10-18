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


@RestController
@RequiredArgsConstructor
public class BoardListController {

    private final BoardListService boardListService;

    // 리스트 생성 - 리스트에 가장 마지막에 추가하는 방식 맨마지막에 추가되도록 구현
    @PostMapping("/boards/{boardId}/lists")
    public ResponseEntity<BoardListSaveResponse> savedBoardList (@RequestBody BoardListSaveRequest boardListSaveRequest, @AuthenticationPrincipal AuthUser authUser, @PathVariable("boardId") Long boardId) {
        BoardListSaveResponse boardListSaveResponse =  boardListService.savedBoardList(boardListSaveRequest, authUser, boardId);
        return ResponseEntity.ok(boardListSaveResponse);
    }

    // 리스트 수정
    @PutMapping("/boards/{boardId}/lists/{boardListId}")
    public ResponseEntity<BoardListSaveResponse> updateBoardList(
            @PathVariable Long boardListId,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody BoardListUpdateRequest boardListUpdateRequest,
            @PathVariable Long boardId
    ) {
        BoardListSaveResponse boardListResponse = boardListService.updateBoardList(boardListId, boardListUpdateRequest, authUser, boardId);
        return ResponseEntity.ok(boardListResponse);
    }

    // 리스트 삭제
    @DeleteMapping("/boards/{boardId}/lists/{boardListId}")
    public void deleteBoardList(
            @PathVariable Long boardListId,
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long boardId
    ) {
        boardListService.deleteBoardList(boardListId, authUser, boardId);
    }

    //기존 리스트의 순서변경
    @PutMapping("/boards/{boardId}/lists/insert/{listId}/order")
    public ResponseEntity<String> changeBoardListOrder(
            @PathVariable Long boardId,
            @PathVariable Long listId,
            @RequestParam Integer newOrder,
            @AuthenticationPrincipal AuthUser authUser) {

        boardListService.changeBoardListOrder(boardId, listId, newOrder, authUser);
        return ResponseEntity.ok("BoardList order changed successfully to " + newOrder + ".");
    }




}
