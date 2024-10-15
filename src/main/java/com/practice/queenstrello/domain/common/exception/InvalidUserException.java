package com.practice.queenstrello.domain.common.exception;

import lombok.Getter;

@Getter
public class InvalidUserException extends RuntimeException {
    private final ErrorCode errorCode;

    public InvalidUserException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}