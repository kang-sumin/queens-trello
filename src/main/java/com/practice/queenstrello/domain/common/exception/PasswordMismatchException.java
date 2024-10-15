package com.practice.queenstrello.domain.common.exception;

import lombok.Getter;

@Getter
public class PasswordMismatchException extends RuntimeException {
    private final ErrorCode errorCode;

    public PasswordMismatchException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}