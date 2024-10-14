package com.practice.queenstrello.domain.card.controller;

import com.practice.queenstrello.domain.card.dto.request.CardSaveRequest;
import com.practice.queenstrello.domain.card.dto.response.CardSaveResponse;
import com.practice.queenstrello.domain.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lists/{listId}/cards")
public class CardController {
    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardSaveResponse> saveCard(@RequestBody CardSaveRequest cardSaveRequest, @PathVariable long listId, @RequestParam Long creatorId){
        CardSaveResponse cardSaveResponse = cardService.saveCard(cardSaveRequest,listId,creatorId);
        return new ResponseEntity.ok(cardSaveResponse);
    }

}
