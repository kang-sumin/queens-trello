package com.practice.queenstrello.domain.user.entity;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.common.entity.CreatedTimestamped;
import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.rmi.ServerException;

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

    // Service 로직에서 UserRepository를 주입받지 User객체를 생성하지 않고 인증된 AuthUser로 User 객체를 받아오기 위해서 사용
    public static User fromAuthUser(AuthUser authUser) {
        UserRole role = UserRole.of(
                authUser.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElseThrow(() -> new QueensTrelloException(ErrorCode.USER_HAS_NOT_PERMISSION))
        );
        return new User(authUser.getUserId(), authUser.getEmail(), authUser.getNickname(), role);
    }

    private User(Long id, String email, String nickname ,UserRole userRole) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.userRole = userRole;
    }

    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }

}
