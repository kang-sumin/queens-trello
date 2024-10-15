package com.practice.queenstrello.domain.common.exception;

import lombok.Getter;

@Getter
public class NoNicnameUserException extends RuntimeException {
    private ErrorCode errorCode;

    public NoNicnameUserException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
