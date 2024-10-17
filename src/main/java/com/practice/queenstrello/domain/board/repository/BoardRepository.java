package com.practice.queenstrello.domain.board.repository;

import com.practice.queenstrello.domain.board.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository <Board, Long> {
    List<Board> findByWorkspaceId(Long workspaceId);
//workspace_id 쿼리 생성
//보드의 워크스페이스 ID 업데이트(JPQL 사용)
    @Modifying
    @Transactional
    @Query("UPDATE Board b set b.workspace =:workspaceId WHERE b.id = :boardId")
    void updateWorkspace(Long boardId, Long workspaceId);
}
