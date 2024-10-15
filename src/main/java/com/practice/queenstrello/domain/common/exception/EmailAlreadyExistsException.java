package com.practice.queenstrello.domain.common.exception;

import lombok.Getter;

@Getter
public class EmailAlreadyExistsException extends RuntimeException {
  private final ErrorCode errorCode;

  public EmailAlreadyExistsException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}