package com.practice.queenstrello.domain.workspace.repository;

import com.practice.queenstrello.domain.workspace.entity.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    @Query("SELECT w FROM Workspace  w WHERE w.id IN :workspaceIds")
    Page<Workspace> findByWorkspaceId(@Param("workspaceIds") List<Long> workspaceIds, Pageable pageable);
}
