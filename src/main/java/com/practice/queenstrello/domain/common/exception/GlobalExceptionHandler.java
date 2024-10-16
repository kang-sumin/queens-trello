package com.practice.queenstrello.domain.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @param ex : 오류상태, 메시지
     * @return 해당 내용이 담긴 에러 객체 반환
     */
    @ExceptionHandler(QueensTrelloException.class)
    public ResponseEntity<Map<String, Object>> handlePasswordMismatchException(QueensTrelloException ex) {
        return getErrorResponse(ex.getErrorCode().getStatus(), ex.getMessage());
    }

    @ExceptionHandler(NoNicnameUserException.class)
    public ResponseEntity<Map<String, Object>> handlePasswordMismatchException(NoNicnameUserException ex) {
        return getErrorResponse(ex.getErrorCode().getStatus(), ex.getMessage());
    }

    /**
     * @param status : 오류 상태 코드
     * @param message : 오류 메시지
     * @return 해당 내용이 담긴 에러 ResponseEntity 객체
     */
    public ResponseEntity<Map<String, Object>> getErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.name());
        errorResponse.put("code", status.value());
        errorResponse.put("message", message);

        return new ResponseEntity<>(errorResponse, status);
    }
}
