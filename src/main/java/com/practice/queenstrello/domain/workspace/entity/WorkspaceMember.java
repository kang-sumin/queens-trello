package com.practice.queenstrello.domain.workspace.entity;

import com.practice.queenstrello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="workspace_members")
public class WorkspaceMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="workspace_member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="member_role")
    private MemberRole memberRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    public WorkspaceMember(MemberRole memberRole, User member, Workspace workspace) {
        this.memberRole = memberRole;
        this.member = member;
        this.workspace = workspace;
    }

    public void updateMemberRole(MemberRole updateMemberRole) {
        this.memberRole = updateMemberRole;
    }
}
