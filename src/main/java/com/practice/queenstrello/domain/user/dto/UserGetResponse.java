package com.practice.queenstrello.domain.user.dto;

import com.practice.queenstrello.domain.user.entity.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGetResponse {

    private Long id;
    private String email;
    private UserRole userRole;

    public UserGetResponse(UserRole userRole, String email, Long id) {
        this.userRole = userRole;
        this.email = email;
        this.id = id;
    }
}