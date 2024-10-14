package com.practice.queenstrello.domain.common.exception;

import lombok.Getter;

@Getter
public class QueensTrelloException extends RuntimeException {
    private ErrorCode errorCode;

    public QueensTrelloException(ErrorCode errorCode) {

    }

}
