package com.practice.queenstrello.domain.card.dto;

import lombok.Getter;

@Getter
public class ManagerResponse {
    String managerNickname;

    public ManagerResponse(String managerNickname) {
        this.managerNickname = managerNickname;
    }
}
