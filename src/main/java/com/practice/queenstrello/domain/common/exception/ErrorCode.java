package com.practice.queenstrello.domain.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // User ErrorCode


    // Attachment ErrorCode


    // MemberRole Errorcode


    // Workspace Errorcode
    HAS_NOT_ACCESS_PERMISSION_MASTER_REQUEST(HttpStatus.FORBIDDEN, "Master 변경 요청 권한이 없습니다."),


    // Board Errorcode


    // List Errorcode


    // Card Errorcode


    // Comment Errorcode


    // Search Errorcode


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
