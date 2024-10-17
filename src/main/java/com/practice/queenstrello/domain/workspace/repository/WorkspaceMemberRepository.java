package com.practice.queenstrello.domain.workspace.repository;

import com.practice.queenstrello.domain.workspace.entity.MemberRole;
import com.practice.queenstrello.domain.workspace.entity.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Long> {
    boolean existsByMemberIdAndWorkspaceIdAndMemberRole(Long userId, Long workspaceId, MemberRole memberRole);

    boolean existsByMemberIdAndWorkspaceId(Long userId, Long workspaceId);

    Optional<WorkspaceMember> findByMemberIdAndWorkspaceId(Long userId, Long workspaceId);

    @Query("SELECT wm.workspace.id FROM WorkspaceMember wm WHERE wm.member.id = :memberId")
    Optional<List<Long>> findWorkspaceIdByMemberId(@Param("memberId") Long memberId);
}
