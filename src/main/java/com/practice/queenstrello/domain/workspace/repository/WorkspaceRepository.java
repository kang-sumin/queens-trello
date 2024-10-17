package com.practice.queenstrello.domain.workspace.repository;

import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    Optional<Workspace> findByMasterUser(User master);
}
