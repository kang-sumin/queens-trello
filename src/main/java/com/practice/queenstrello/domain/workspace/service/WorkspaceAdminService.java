package com.practice.queenstrello.domain.workspace.service;

import com.practice.queenstrello.domain.workspace.dto.response.MasterRequestResponse;
import com.practice.queenstrello.domain.workspace.entity.MasterRequest;
import com.practice.queenstrello.domain.workspace.repository.MasterRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkspaceAdminService {

    private final MasterRequestRepository masterRequestRepository;

    // 승인되지 않은 Master 권한 변경 요청 내역 조회 (다건 조회)
    public Page<MasterRequestResponse> getMasterRequests(int page, int size, AuthUser authUser) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<MasterRequest> masterRequests = findMasterRequests()

        return null;
    }
}
