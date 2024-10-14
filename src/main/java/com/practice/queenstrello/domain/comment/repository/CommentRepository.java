package com.practice.queenstrello.domain.comment.repository;

import com.practice.queenstrello.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
