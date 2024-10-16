package com.practice.queenstrello.domain.workspace.entity;

import com.practice.queenstrello.domain.board.entity.Board;
import com.practice.queenstrello.domain.common.entity.ModifiedTimestamped;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.entity.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "workspaces")
public class Workspace extends ModifiedTimestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workspace_id")
    private Long id;

    @Column(name = "workspace_name", nullable = false, length = 100)
    private String name;

    @Column(name = "workspace_description", length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_workspace_user_id", nullable = false)
    private User createUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id", nullable = false)
    private User masterUser;

    @OneToMany(mappedBy = "workspace")
    private List<WorkspaceMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "workspace")
    private List<Board> boards = new ArrayList<>();


    public Workspace(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.createUser = user;
        this.masterUser = user;
    }
}
