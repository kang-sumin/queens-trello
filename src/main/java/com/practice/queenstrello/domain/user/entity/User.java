package com.practice.queenstrello.domain.user.entity;

import com.practice.queenstrello.domain.common.entity.CreatedTimestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="users")
public class User extends CreatedTimestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @Column(name="user_email", unique=true, nullable=false, length=255)
    private String email;

    @Column(name="password", nullable=false, length=255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private UserRole userRole;

    @Column(name="is_deleted",nullable=false)
    private Boolean isDeleted;

    @Column(name="nickname", nullable=true, length=100)
    private String nickname;



}
