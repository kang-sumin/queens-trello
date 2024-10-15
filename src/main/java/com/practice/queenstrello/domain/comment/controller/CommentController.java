package com.practice.queenstrello.domain.comment.controller;

import com.practice.queenstrello.domain.comment.dto.request.CommentSaveRequest;
import com.practice.queenstrello.domain.comment.dto.response.CommentSaveResponse;
import com.practice.queenstrello.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards/{cardId}/comments")
public class CommentController {
    private final CommentService commentService;

    //댓글 작성
    @PostMapping
    public ResponseEntity<CommentSaveResponse> saveComment(@PathVariable long cardId, @RequestBody CommentSaveRequest commentSaveRequest) {
        CommentSaveResponse response = commentService.saveComment(cardId, commentSaveRequest);
        return ResponseEntity.ok(response);
    }
}
