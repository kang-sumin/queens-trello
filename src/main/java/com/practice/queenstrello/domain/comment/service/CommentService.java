package com.practice.queenstrello.domain.comment.service;

import com.practice.queenstrello.domain.card.entity.Card;
import com.practice.queenstrello.domain.card.repository.CardRepository;
import com.practice.queenstrello.domain.comment.dto.request.CommentSaveRequest;
import com.practice.queenstrello.domain.comment.dto.response.CommentSaveResponse;
import com.practice.queenstrello.domain.comment.entity.Comment;
import com.practice.queenstrello.domain.comment.repository.CommentRepository;
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


    public CommentSaveResponse saveComment(long cardId, CommentSaveRequest commentSaveRequest) {
        //댓글 작성자 확인
        User user = userRepository.findById(commentSaveRequest.getUserId()).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID 입니다."));

        //읽기 전용 사용자 예외처리
        if (user.getUserRole().equals(UserRole.ROLE_USER)) {
            throw new IllegalStateException("읽기 전용 사용자는 댓글 작성 권한이 없습니다.");
        }
        //카드 존재 여부 확인
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카드 ID입니다."));

        //댓글 생성
        Comment comment = new Comment(commentSaveRequest.getContent(), card, user);
        commentRepository.save(comment);

        return new CommentSaveResponse(comment.getId(),
                comment.getContent(),
                comment.getId(),
                comment.getCreatedAt());

    }
}
