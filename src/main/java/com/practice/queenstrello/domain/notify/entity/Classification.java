package com.practice.queenstrello.domain.notify.entity;

import lombok.Getter;

@Getter
public enum Classification {
    Master("마스터로 승급을 축하합니다!"),
    Invite("님이 당신을 워크스페이스로 초대하셨습니다"),
    Member("워크스페이스에 새로운 멤버가 초대되었습니다"),
    Card("카드 내용이 변경되었습니다, 지금 당장 확인하세요!"),
    Comment("댓글이 달렸습니다.");

    private final String title;

    Classification(String title){
        this.title = title;
    }
}
