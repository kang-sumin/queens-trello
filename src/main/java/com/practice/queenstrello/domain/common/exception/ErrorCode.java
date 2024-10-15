package com.practice.queenstrello.domain.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // User ErrorCode


    // Attachment ErrorCode


    // MemberRole Errorcode


    // Workspace Errorcode


    // Board Errorcode


    // List Errorcode


    // Card Errorcode


    // Comment Errorcode


    // Search Errorcode
    NOT_FOUND_NICKNAME(HttpStatus.NOT_FOUND, "해당 닉네임의 유저가 존재하지 않습니다."),


    // Notification Errorcode



    // ### 아래 코드 위에 ErrorCode 작성해 주세요! ErrorCode 메서드 사이는 ,(컴마)로 구분해 주세요! ###
    NOT_FOUND(HttpStatus.NOT_FOUND, "찾지못했습니다.");



    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message){
        this.status = httpStatus;
        this.message = message;
    }
}
