package com.practice.queenstrello.domain.auth.controller;

import com.practice.queenstrello.domain.auth.dto.request.AuthSignupRequest;
import com.practice.queenstrello.domain.auth.dto.response.AuthSignupResponse;
import com.practice.queenstrello.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    //회원가입
    @PostMapping("/auth/signup")
    public AuthSignupResponse signup(@Valid @RequestBody AuthSignupRequest authSignupRequest) {
        return authService.signup(authSignupRequest);
    }
}

