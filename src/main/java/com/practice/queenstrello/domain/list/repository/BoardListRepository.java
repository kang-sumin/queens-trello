package com.practice.queenstrello.domain.list.repository;

import com.practice.queenstrello.domain.list.entity.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardListRepository extends JpaRepository<BoardList,Long> {
}
