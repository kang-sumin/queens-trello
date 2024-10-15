package com.practice.queenstrello.domain.auth.service;

import com.practice.queenstrello.domain.auth.dto.request.AuthSignupRequest;
import com.practice.queenstrello.domain.auth.dto.response.AuthSignupResponse;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.entity.UserRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthSignupResponse signup(@Valid AuthSignupRequest authSignupRequest) {
        if (userRepository.existsByEmail(authSignupRequest.getEmail())) {
            throw new InvalidRequestException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        UserRole userRole = UserRole.of(authSignupRequests.getUserRole());

        User newUser = new User(
                authSignupRequest.getEmail(),
                encodedPassword,
                authSignupRequest.getNickname(),
                userRole
        );
        User savedUser = userRepository.save(newUser);

        String bearerToken = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), savedUser.getNickname(), userRole);

        return new AuthSignupResponse(bearerToken);

    }
}
