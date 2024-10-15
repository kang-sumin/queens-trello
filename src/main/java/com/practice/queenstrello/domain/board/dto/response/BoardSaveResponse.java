package com.practice.queenstrello.domain.board.dto.response;

import com.practice.queenstrello.domain.board.entity.Board;
import com.practice.queenstrello.domain.card.entity.Card;
import com.practice.queenstrello.domain.list.entity.BoardList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Getter
public class BoardSaveResponse {
    private final Long id;
    private final String title;
    private final String backgroundColor;
    private final String imageUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private List<Card> cards; //카드 응답 추가

    public static BoardSaveResponse of(Board board) {
        //Board의 BoardList에서 Card를 수집
        List<Card> cards = board.getBoardLists().stream()
                .flatMap(boardList -> boardList.getCards().stream())
                .collect(Collectors.toList());//모든 카드 수집

        return new BoardSaveResponse(
                board.getId(),
                board.getTitle(),
                board.getBackgroundColor(),
                board.getImageUrl(),
                board.getCreatedAt(),
                board.getModifiedAt(),
                cards //카드리스트 추가
        );
    }
    //생성자
    public BoardSaveResponse(Long id, String title, String backgroundColor, String imageUrl,LocalDateTime createdAt, LocalDateTime modifiedAt, List<Card> cards) {
        this.id = id;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.cards = cards;
    }
}
