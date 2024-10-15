package com.practice.queenstrello.domain.list.dto.response;

import com.practice.queenstrello.domain.list.entity.BoardList;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardListSaveResponse {
    private final Long id;
    private final String title;
    private final Integer order;
    //BoardList entity 를 BoardListSaveResponse로 변환하는 정적 팩토리 메서드
    public static BoardListSaveResponse of(BoardList savedBoardList) {
        return new BoardListSaveResponse(
                savedBoardList.getId(),
                savedBoardList.getTitle(),
                savedBoardList.getOrder()
        );
    }
}
