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

    //멤버가 해당 워크스페이스에 속해있는 멤버가 맞는지 검증하는 쿼리메소드
    boolean existsByMemberIdAndWorkspaceId(Long userId, Long workspaceId);

    Optional<WorkspaceMember> findByMemberIdAndWorkspaceId(Long userId, Long workspaceId);

//    Optional<List<Long>> findWorkspaceIdByMemberId(Long id);

//    @Query("SELECT wm.workspace.id FROM WorkspaceMember wm " +
//            "JOIN Workspace w ON wm.workspace.id = w.id " +
//            "WHERE wm.member.id = :userId OR w.createUser.id = :userId")
//    Optional<List<Long>> findWorkspaceIdByUserId(@Param("userId") Long userId);

    @Query("SELECT wm.workspace.id FROM WorkspaceMember wm WHERE wm.member.id = :memberId")
    Optional<List<Long>> findWorkspaceIdByMemberId(@Param("memberId") Long memberId);
}
