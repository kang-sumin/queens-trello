package com.practice.queenstrello.domain.search.dto;

import lombok.Getter;

@Getter
public class ManagerResponse {
    private Long managerId;
    private String managerNickname;

    public ManagerResponse(Long managerId, String managerNickname) {
        this.managerId = managerId;
        this.managerNickname = managerNickname;
    }
}
