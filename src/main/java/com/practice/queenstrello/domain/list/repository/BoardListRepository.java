package com.practice.queenstrello.domain.list.repository;

import com.practice.queenstrello.domain.list.entity.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoardListRepository extends JpaRepository<BoardList, Long> {
    List<BoardList> findByBoardIdOrderByOrderAsc(Long boardId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Card c WHERE c.boardList.id = :listId")
    void deleteAllCardsByBoardListId(Long listId);
}
