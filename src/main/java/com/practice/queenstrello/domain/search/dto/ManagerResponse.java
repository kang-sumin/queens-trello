package com.practice.queenstrello.domain.search.dto;

import lombok.Getter;

@Getter
public class ManagerResponse {
    String managerNickname;

    public ManagerResponse(String managerNickname) {
        this.managerNickname = managerNickname;
    }
}
