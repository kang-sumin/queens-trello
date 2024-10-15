package com.practice.queenstrello.domain.user.service;

import com.practice.queenstrello.config.AuthUser;
import com.practice.queenstrello.domain.user.dto.UserGetResponse;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;


    public UserGetResponse getUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidRequestException("User not found"));
        return new UserGetResponse(user.getUserRole(), user.getEmail(), user.getId());
    }
}