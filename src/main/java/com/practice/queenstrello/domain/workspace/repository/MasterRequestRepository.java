package com.practice.queenstrello.domain.workspace.repository;

import com.practice.queenstrello.domain.workspace.entity.MasterRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MasterRequestRepository extends JpaRepository<MasterRequest, Long> {

    boolean existsByRequestUserId(Long id);

    Optional<MasterRequest> findByRequestUserId(Long userId);
}
