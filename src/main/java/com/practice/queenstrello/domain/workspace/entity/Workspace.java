package com.practice.queenstrello.domain.workspace.entity;

import com.practice.queenstrello.domain.board.entity.Board;
import com.practice.queenstrello.domain.common.entity.ModifiedTimestamped;
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
@Table(name="workspaces")
public class Workspace extends ModifiedTimestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="workspace_id")
    private Long id;

    @Column(name="workspace_name", nullable=false, length=100)
    private String name;

    @Column(name="workspace_description", nullable=false, length=500)
    private String description;

    @OneToMany(mappedBy = "workspace")
    private List<WorkspaceMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "workspace")
    private List<Board> boards = new ArrayList<>();


}
