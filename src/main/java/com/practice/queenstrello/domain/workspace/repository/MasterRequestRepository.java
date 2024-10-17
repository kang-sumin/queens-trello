package com.practice.queenstrello.domain.workspace.repository;

import com.practice.queenstrello.domain.workspace.entity.MasterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MasterRequestRepository extends JpaRepository<MasterRequest, Long> {

    boolean existsByRequestUserId(Long id);

    Optional<MasterRequest> findByRequestUserId(Long userId);

    @Query("SELECT mr FROM MasterRequest  mr WHERE mr.isAccepted = false ORDER BY mr.createdAt ASC")
    Optional<Page<MasterRequest>> findAllByIsAcceptedFalse(Pageable pageable);
}
