package com.practice.queenstrello.domain.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //Auth ErrorCode
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "가입되지 않은 유저입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다."),
    // User ErrorCode
    INVALID_USER(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자입니다."),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

    // Attachment ErrorCode


    // MemberRole Errorcode


    // Workspace Errorcode


    // Board Errorcode
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 보드를 찾을 수 없습니다."),
    INVALID_AUTHORITY_UPDATE(HttpStatus.NOT_ACCEPTABLE, "보드를 수정 할 권한이 없습니다."),
    INVALID_AUTHORITY_DELETE(HttpStatus.NOT_ACCEPTABLE,"보드를 삭제 할 권한이 없습니다."),
    INVALID_AUTHORITY_CREATE(HttpStatus.NOT_ACCEPTABLE,"보드를 생성 할 권한이 없습니다."),
    TITLE_ESSENTIAL(HttpStatus.CREATED,"보드 제목은 필수입니다."),

    // List Errorcode
    BOARDLIST_NOT_FOUND(HttpStatus.NOT_FOUND,"리스트가 없습니다."),

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
