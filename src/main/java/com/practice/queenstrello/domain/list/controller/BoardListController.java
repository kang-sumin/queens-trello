package com.practice.queenstrello.domain.list.controller;

import com.practice.queenstrello.domain.list.dto.request.BoardListSaveRequest;
import com.practice.queenstrello.domain.list.dto.request.BoardListUpdateRequest;
import com.practice.queenstrello.domain.list.dto.response.BoardListSaveResponse;
import com.practice.queenstrello.domain.list.service.BoardListService;
import com.practice.queenstrello.domain.workspace.entity.WorkspaceMember;
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
        BoardListSaveResponse boardListSaveResponse =  boardListService.savedBoardList(boardListSaveRequest, new WorkspaceMember());
        return ResponseEntity.ok(boardListSaveResponse);
    }

    @PutMapping("/{listId}")
    public ResponseEntity<BoardListSaveResponse> updateBoardList(
            @PathVariable long boardListId,
            @RequestBody BoardListUpdateRequest boardListUpdateRequest
    ) {
        BoardListSaveResponse boardListResponse = boardListService.updateBoardList(boardListId, boardListUpdateRequest, new WorkspaceMember());
        return ResponseEntity.ok(boardListResponse);
    }

    @DeleteMapping("/{listId}")
    public void deleteBoardList(
            @PathVariable long boardListId
    ) {
        boardListService.deleteBoardList(boardListId, new WorkspaceMember());
    }


}
