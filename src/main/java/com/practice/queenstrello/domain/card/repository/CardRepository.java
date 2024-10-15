package com.practice.queenstrello.domain.card.repository;

import com.practice.queenstrello.domain.card.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends JpaRepository<Card,Long> {
    //어떤 리스트ID 하나에 속한 카드를 페이징 처리해서 조회
   // Page<Card> findByListId(Long listId, Pageable pageable);

    @Query("SELECT c FROM Card c " +
    "LEFT JOIN FETCH c.cardManagers cm " + //card와 관련된 cardmanager엔티티랑 조인
    "LEFT JOIN FETCH cm.manager " + //cardmanager와 연결된 user(담당자정보)엔티티랑 조인
    "WHERE c.boardList.id = :listId")
    Page<Card> findByListIdWithManagers(@Param("listId") Long listId, Pageable pageable);
}
