package com.practice.queenstrello.domain.auth;
import com.practice.queenstrello.domain.user.entity.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collection;
import java.util.List;
@Getter
public class AuthUser {
    private final Long userId;
    private final String email;
    private final String nickname;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(Long userId, String email, String nickname, UserRole role) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;

        this.authorities = List.of(new SimpleGrantedAuthority(role.name()));
    }
}