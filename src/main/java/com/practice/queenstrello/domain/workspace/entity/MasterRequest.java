package com.practice.queenstrello.domain.workspace.entity;

import com.practice.queenstrello.domain.common.entity.CreatedTimestamped;
import com.practice.queenstrello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name="master_request")
public class MasterRequest extends CreatedTimestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="master_request_id")
    private Long id;

    @Column(name="is_accepted", nullable=false)
    private Boolean isAccepted;

    @OneToOne
    @JoinColumn(name="request_user_id", nullable=false)
    private User requestUser;

    public MasterRequest(Boolean isAccepted, User requestUser) {
        this.isAccepted = isAccepted;
        this.requestUser = requestUser;
    }
}
