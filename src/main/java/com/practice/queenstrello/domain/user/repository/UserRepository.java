package com.practice.queenstrello.domain.user.repository;

import com.practice.queenstrello.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
