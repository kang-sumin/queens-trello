package com.practice.queenstrello.domain.workspace.entity;

import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MemberRole {
    WORKSPACE,
    BOARD,
    READ;

    public static MemberRole of(String role) {
        return Arrays.stream(MemberRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_MEMBER_ROLE));
    }

}
