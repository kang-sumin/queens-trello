package com.practice.queenstrello.domain.user.controller;

import com.practice.queenstrello.domain.user.dto.UserGetResponse;
import com.practice.queenstrello.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    // 사용자 정보 조회
    // 사용자 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserGetResponse> getUser(@PathVariable Long userId) {
        UserGetResponse userGetResponse = userService.getUser(userId);
        return ResponseEntity.ok(userGetResponse);
    }

}