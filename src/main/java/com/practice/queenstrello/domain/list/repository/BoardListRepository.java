package com.practice.queenstrello.domain.list.repository;

import com.practice.queenstrello.domain.list.entity.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoardListRepository extends JpaRepository<BoardList,Long> {
    List<BoardList> findByBoardIdAndOrderGreaterThan(Long boardId, Integer order);

    @Modifying
    @Transactional
    @Query("DELETE FROM Card c WHERE c.boardList.id = :listId")
    void deleteAllCardsByBoardListId(Long listId);

    // 순서가 특정 값보다 크거나 같은 BoardList 조회
    List<BoardList> findByBoardIdAndOrderGreaterThanEqual(Long boardId, Integer order);

    // 순서가 특정 범위에 있는 BoardList 조회
    List<BoardList> findByBoardIdAndOrderBetween(Long boardId, Integer startOrder, Integer endOrder);
}
