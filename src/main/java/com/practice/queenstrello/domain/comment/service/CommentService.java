package com.practice.queenstrello.domain.comment.service;

import com.practice.queenstrello.domain.card.entity.Card;
import com.practice.queenstrello.domain.card.repository.CardRepository;
import com.practice.queenstrello.domain.comment.dto.request.CommentSaveRequest;
import com.practice.queenstrello.domain.comment.dto.request.CommentUpdateRequest;
import com.practice.queenstrello.domain.comment.dto.response.CommentSaveResponse;
import com.practice.queenstrello.domain.comment.dto.response.CommentUpdateResponse;
import com.practice.queenstrello.domain.comment.entity.Comment;
import com.practice.queenstrello.domain.comment.repository.CommentRepository;
import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.common.aop.annotation.SlackComment;
import com.practice.queenstrello.domain.user.repository.UserRepository;
import com.practice.queenstrello.domain.workspace.entity.MemberRole;
import com.practice.queenstrello.domain.workspace.repository.WorkspaceMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    //댓글 작성
    @Transactional
    @SlackComment
    public CommentSaveResponse saveComment(CommentSaveRequest commentSaveRequest , Long cardId, Long userId, Long workspaceId) {

        //댓글 작성자 확인 - 워크스페이스 멤버 여부 확인
        boolean isMember = workspaceMemberRepository.existsByMemberIdAndWorkspaceId(userId, workspaceId);
        if (!isMember) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION);
        }

        // 워크스페이스 내에서 유저의 권한을 확인 -> READ 권한만 있는지 확인
        boolean isReadOnly = workspaceMemberRepository.existsByMemberIdAndWorkspaceIdAndMemberRole(userId, workspaceId, MemberRole.READ);
        if (isReadOnly) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION);
        }

        //카드 존재 여부 확인
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_CARD));

        //댓글 생성
        Comment comment = new Comment(commentSaveRequest.getContent(), card, userRepository.findById(userId).orElseThrow(()-> new QueensTrelloException(ErrorCode.INVALID_USER)));
        commentRepository.save(comment);

        CommentSaveResponse a = null;
        try {
            a = new CommentSaveResponse(comment.getId(),
                    comment.getContent(),
                    comment.getId(),
                    comment.getCreatedAt());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return a;

    }

    //댓글 수정 남이 못하게 수정도~
    @Transactional
    public CommentUpdateResponse updateComment(CommentUpdateRequest commentUpdateRequest, Long commentId, Long userId, Long workspaceId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new QueensTrelloException(ErrorCode.INVALID_COMMENT));

        // 워크스페이스 멤버 여부 확인
        boolean isMember = workspaceMemberRepository.existsByMemberIdAndWorkspaceId(userId, workspaceId);
        if (!isMember) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION); // 워크스페이스에 속해 있지 않으면 예외 처리
        }

        // READ 권한 여부 확인
        boolean isReadOnly = workspaceMemberRepository.existsByMemberIdAndWorkspaceIdAndMemberRole(userId, workspaceId, MemberRole.READ);
        if (isReadOnly) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION); // READ 권한만 있을 경우 예외 처리
        }

        // 댓글 작성자 확인(본인 댓글만 수정 가능)
        if (!comment.getUser().getId().equals(userId)){
            throw new QueensTrelloException(ErrorCode.INVALID_COMMENTUSER);
        }

        // 댓글 내용 수정
        comment.updateContent(commentUpdateRequest.getContent()); //comment엔티티의 updatecontent메서드로 수정

        // 수정된 댓글 정보 반환
        return new CommentUpdateResponse(comment.getId(),
                comment.getContent(),
                comment.getModifiedAt());
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long userId, Long workspaceId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new IllegalArgumentException("유효하지 않은 댓글ID 입니다."));

        //  워크스페이스 멤버 여부 확인
        boolean isMember = workspaceMemberRepository.existsByMemberIdAndWorkspaceId(userId, workspaceId);
        if (!isMember) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION); // 워크스페이스에 속해 있지 않으면 예외 처리
        }

        //  READ 권한 여부 확인
        boolean isReadOnly = workspaceMemberRepository.existsByMemberIdAndWorkspaceIdAndMemberRole(userId, workspaceId, MemberRole.READ);
        if (isReadOnly) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION); // READ 권한만 있을 경우 예외 처리
        }

        // 댓글 작성자 확인 (본인만 삭제 가능)
        if (!comment.getUser().getId().equals(userId)) {
            throw new QueensTrelloException(ErrorCode.INVALID_COMMENTUSER); // 본인이 작성한 댓글이 아니면 예외 처리
        }

        commentRepository.delete(comment);
    }
}
