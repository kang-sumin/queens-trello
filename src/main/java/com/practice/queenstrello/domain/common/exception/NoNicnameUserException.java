package com.practice.queenstrello.domain.common.exception;

import lombok.Getter;

@Getter
public class NoNicnameUserException extends RuntimeException {
    public NoNicnameUserException(String message) {
        super(message);
    }
}
