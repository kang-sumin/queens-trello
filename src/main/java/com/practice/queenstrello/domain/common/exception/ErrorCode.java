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
    USER_HAS_NOT_PERMISSION(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // Attachment ErrorCode


    // MemberRole Errorcode


    // Workspace Errorcode
    HAS_NOT_ACCESS_PERMISSION_MASTER_REQUEST(HttpStatus.FORBIDDEN, "Master 변경 요청 권한이 없습니다."),
    MASTER_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "Master 요청 내역이 없습니다."),
    MASTER_REQUEST_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 Master 권한 요청 내역이 존재합니다."),
    HAS_NOT_ACCESS_PERMISSION(HttpStatus.CONFLICT, "해당 서비스에 접근 권한이 없습니다."),


    // Board Errorcode


    // List Errorcode
    INVALID_LIST(HttpStatus.BAD_REQUEST,"유효하지 않은 listId 입니다."),

    // Card Errorcode
    INVALID_USERROLE(HttpStatus.CONFLICT,"읽기 전용 사용자는 권한이 없습니다."),
    INVALID_CARD(HttpStatus.BAD_REQUEST,"유효하지 않은 cardId 입니다."),

    // Comment Errorcode
    INVALID_COMMENT(HttpStatus.BAD_REQUEST,"유효하지 않은 commentId 입니다."),
    INVALID_COMMENTUSER(HttpStatus.METHOD_NOT_ALLOWED,"본인 댓글만 수정 및 삭제가 가능합니다."),

    // Search Errorcode
    NOT_FOUND_NICKNAME(HttpStatus.NOT_FOUND, "해당 닉네임의 유저가 존재하지 않습니다."),


    // Notification Errorcode


    // ### 아래 코드 위에 ErrorCode 작성해 주세요! ErrorCode 메서드 사이는 ,(컴마)로 구분해 주세요! ###
    NOT_FOUND(HttpStatus.NOT_FOUND, "찾지못했습니다.");


    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.status = httpStatus;
        this.message = message;
    }
}
