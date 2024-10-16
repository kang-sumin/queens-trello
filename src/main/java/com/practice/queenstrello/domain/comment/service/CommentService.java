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
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.entity.UserRole;
import com.practice.queenstrello.domain.user.repository.UserRepository;
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

    //댓글 작성
    @Transactional
    public CommentSaveResponse saveComment(CommentSaveRequest commentSaveRequest , Long cardId, Long userId) {

        //댓글 작성자 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_USER));

        //읽기 전용 사용자 예외처리
        if (user.getUserRole().equals(UserRole.ROLE_USER)) {
            throw new QueensTrelloException(ErrorCode.INVALID_USERROLE);
        }
        //카드 존재 여부 확인
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_CARD));

        //댓글 생성
        Comment comment = new Comment(commentSaveRequest.getContent(), card, user);
        commentRepository.save(comment);

        return new CommentSaveResponse(comment.getId(),
                comment.getContent(),
                comment.getId(),
                comment.getCreatedAt());

    }

    //댓글 수정
    @Transactional
    public CommentUpdateResponse updateComment(CommentUpdateRequest commentUpdateRequest, Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new QueensTrelloException(ErrorCode.INVALID_COMMENT));

        if (!comment.getUser().getId().equals(userId)){
            throw new QueensTrelloException(ErrorCode.INVALID_COMMENTUSER);
        }
        comment.updateContent(commentUpdateRequest.getContent()); //comment엔티티의 updatecontent메서드로 수정
        return new CommentUpdateResponse(comment.getId(),
                comment.getContent(),
                comment.getModifiedAt());
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new IllegalArgumentException("유효하지 않은 댓글ID 입니다."));
        if (!comment.getUser().getId().equals(userId)) {
            throw new QueensTrelloException(ErrorCode.INVALID_COMMENTUSER);
        }
        commentRepository.delete(comment);
    }
}
