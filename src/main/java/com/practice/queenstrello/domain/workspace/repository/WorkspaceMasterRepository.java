package com.practice.queenstrello.domain.workspace.repository;

import com.practice.queenstrello.domain.workspace.entity.MasterRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceMasterRepository extends JpaRepository<MasterRequest, Long> {

}
