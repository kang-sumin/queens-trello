package com.practice.queenstrello.domain.user.service;


import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void deleteUser(Long userId, String password) {
        // 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_USER));

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new QueensTrelloException(ErrorCode.PASSWORD_MISMATCH);
        }

        // 논리적 삭제 처리 (isDeleted를 true로 설정)
        user.setIsDeleted(true);//메서드명 변경
        // 사용자 정보를 업데이트
        userRepository.save(user);//수정
}

}