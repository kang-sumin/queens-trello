package com.practice.queenstrello.domain.comment.controller;

import com.practice.queenstrello.domain.comment.dto.request.CommentSaveRequest;
import com.practice.queenstrello.domain.comment.dto.request.CommentUpdateRequest;
import com.practice.queenstrello.domain.comment.dto.response.CommentSaveResponse;
import com.practice.queenstrello.domain.comment.dto.response.CommentUpdateResponse;
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
    public ResponseEntity<CommentSaveResponse> saveComment(@RequestBody CommentSaveRequest commentSaveRequest,@PathVariable Long cardId, @RequestParam Long userId) {
        CommentSaveResponse response = commentService.saveComment(commentSaveRequest,cardId,userId);
        return ResponseEntity.ok(response);
    }

    //댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentUpdateResponse> updateComment(@RequestBody CommentUpdateRequest commentUpdateRequest, @PathVariable Long commentId, @RequestParam Long userId){
        CommentUpdateResponse response = commentService.updateComment(commentUpdateRequest,commentId,userId);
        return ResponseEntity.ok(response);
    }

}
