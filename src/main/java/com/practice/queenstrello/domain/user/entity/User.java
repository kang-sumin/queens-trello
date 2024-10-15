package com.practice.queenstrello.domain.user.entity;

import com.practice.queenstrello.domain.common.entity.CreatedTimestamped;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    @Column(name="user_nickname", nullable = false, length = 50)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private UserRole userRole;

    @Column(name="is_deleted",nullable=false)
    private Boolean isDeleted = Boolean.FALSE;



    public User(@NotBlank @Email String email, String encodedPassword, @NotBlank String nickname, UserRole userRole) {
        this.email = email;
        this.password = encodedPassword;
        this.nickname = nickname;
        this.userRole = userRole;
    }
}
