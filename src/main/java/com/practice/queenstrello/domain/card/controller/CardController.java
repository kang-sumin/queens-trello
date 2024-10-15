package com.practice.queenstrello.domain.card.controller;

import com.practice.queenstrello.domain.card.dto.request.CardSaveRequest;
import com.practice.queenstrello.domain.card.dto.response.CardDetailResponse;
import com.practice.queenstrello.domain.card.dto.response.CardSaveResponse;
import com.practice.queenstrello.domain.card.dto.response.CardSimpleResponse;
import com.practice.queenstrello.domain.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lists/{listId}/cards")
public class CardController {
    private final CardService cardService;

    //카드 생성
    @PostMapping
    public ResponseEntity<CardSaveResponse> saveCard(@RequestBody CardSaveRequest cardSaveRequest, @PathVariable long listId, @RequestParam Long creatorId){
        CardSaveResponse cardSaveResponse = cardService.saveCard(cardSaveRequest,listId,creatorId);
        return ResponseEntity.ok(cardSaveResponse);
    }

    //카드 다건 조회
    @GetMapping
    public ResponseEntity<Page<CardSimpleResponse>> getCards(@PathVariable Long listId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<CardSimpleResponse> cardSimpleResponses = cardService.getCards(listId,page,size);
        return ResponseEntity.ok(cardSimpleResponses);
    }

    //카드 단건 조회
    @GetMapping("/{cardId}")
    public ResponseEntity<CardDetailResponse> getCard(@PathVariable long cardId) {
        CardDetailResponse cardDetailResponse = cardService.getCard(cardId);
        return ResponseEntity.ok(cardDetailResponse);
    }


}
