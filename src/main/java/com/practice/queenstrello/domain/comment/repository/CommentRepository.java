package com.practice.queenstrello.domain.comment.repository;

import com.practice.queenstrello.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    //특정 카드에 연결된 모든 댓글 삭제
    void deleteByCardId(Long cardId);
}
