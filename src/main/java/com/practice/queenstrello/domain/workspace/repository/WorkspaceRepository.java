package com.practice.queenstrello.domain.workspace.repository;

import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.workspace.entity.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    Optional<Workspace> findByMasterUser(User master);

    @Query("SELECT w FROM Workspace  w WHERE w.id IN :workspaceIds")
    Page<Workspace> findByWorkspaceId(@Param("workspaceIds") List<Long> workspaceIds, Pageable pageable);
}
