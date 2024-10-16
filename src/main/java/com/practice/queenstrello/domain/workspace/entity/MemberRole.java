package com.practice.queenstrello.domain.workspace.entity;

import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.user.entity.UserRole;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MemberRole {
    WORKSPACE,
    BOARD,
    READ;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_MEMBER_ROLE));
    }

}
