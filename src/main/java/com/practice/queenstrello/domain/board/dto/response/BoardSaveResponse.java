package com.practice.queenstrello.domain.board.dto.response;

import com.practice.queenstrello.domain.board.entity.Board;
import com.practice.queenstrello.domain.card.entity.Card;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Getter
public class BoardSaveResponse {
    private final Long id;
    private final String title;
    private final String backgroundColor;
    private final String imageUrl;
    private List<Card> cards; //카드 응답 추가



    public static BoardSaveResponse of(Board board) {
        List<CardResponse> cardResponseList = cards.stream()
                .map(CardResponse::of)
                .collect(Collectors.toList());

        return new BoardSaveResponse(
                board.getId(),
                board.getTitle(),
                board.getBackgroundColor(),
                board.getImageUrl());
    }
    //생성자
    public BoardSaveResponse(Long id, String title, String backgroundColor, String imageUrl, List<Card> cards) {
        this.id = id;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.imageUrl = imageUrl;
        this.cards = cards;
    }
}
