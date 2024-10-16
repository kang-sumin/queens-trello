package com.practice.queenstrello.domain.workspace.repository;

import com.practice.queenstrello.domain.workspace.entity.MemberRole;
import com.practice.queenstrello.domain.workspace.entity.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Long> {
//    boolean existsByMemberId(Long userId);

//    Optional<WorkspaceMember> findByMemberIdAndWorkspaceIdAndMemberRole(Long userId, Long workspaceId, MemberRole memberRole);

    boolean existsByMemberIdAndWorkspaceIdAndMemberRole(Long userId, Long workspaceId, MemberRole memberRole);
}
