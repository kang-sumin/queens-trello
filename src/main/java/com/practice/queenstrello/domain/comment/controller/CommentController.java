package com.practice.queenstrello.domain.comment.controller;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.comment.dto.request.CommentSaveRequest;
import com.practice.queenstrello.domain.comment.dto.request.CommentUpdateRequest;
import com.practice.queenstrello.domain.comment.dto.response.CommentSaveResponse;
import com.practice.queenstrello.domain.comment.dto.response.CommentUpdateResponse;
import com.practice.queenstrello.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("workspaces/{workspaceId}/boards/{boardId}/lists/{listId}/cards/{cardId}/comments")
public class CommentController {
    private final CommentService commentService;

    //댓글 작성
    @PostMapping
    public ResponseEntity<CommentSaveResponse> saveComment(@RequestBody CommentSaveRequest commentSaveRequest,@PathVariable Long cardId,@RequestParam Long workspaceId, @AuthenticationPrincipal AuthUser authUser) {
        Long userId = authUser.getUserId();  // 인증된 사용자의 ID 사용
        CommentSaveResponse response = commentService.saveComment(commentSaveRequest,cardId,userId,workspaceId);
        return ResponseEntity.ok(response);
    }

    //댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentUpdateResponse> updateComment(@RequestBody CommentUpdateRequest commentUpdateRequest, @PathVariable Long commentId,@RequestParam Long workspaceId, @AuthenticationPrincipal AuthUser authUser){
        Long userId = authUser.getUserId();  // 인증된 사용자의 ID 사용
        CommentUpdateResponse response = commentService.updateComment(commentUpdateRequest,commentId,userId,workspaceId);
        return ResponseEntity.ok(response);
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @RequestParam Long workspaceId,@AuthenticationPrincipal AuthUser authUser) {
        Long userId = authUser.getUserId();  // 인증된 사용자의 ID 사용
        commentService.deleteComment(commentId,userId,workspaceId);
        return ResponseEntity.noContent().build();
    }

}
