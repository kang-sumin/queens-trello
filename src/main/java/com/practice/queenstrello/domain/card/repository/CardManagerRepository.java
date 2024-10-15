package com.practice.queenstrello.domain.card.repository;

import com.practice.queenstrello.domain.card.entity.CardManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardManagerRepository extends JpaRepository<CardManager,Long> {
    // 카드의 기존 담당자들 조회
    List<CardManager> findByCardId(Long cardId);

    //특정 카드에 연결된 모든 CardManager삭제
    Void deleteByCardId(Long cardId);

    // 특정 카드 안의 특정 담당자 삭제
    void deleteByCardIdAndManagerId(Long cardId, Long managerId);
}
