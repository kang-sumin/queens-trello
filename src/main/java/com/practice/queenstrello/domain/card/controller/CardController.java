package com.practice.queenstrello.domain.card.controller;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.card.dto.request.CardSaveRequest;
import com.practice.queenstrello.domain.card.dto.request.CardUpdateRequest;
import com.practice.queenstrello.domain.card.dto.response.CardDetailResponse;
import com.practice.queenstrello.domain.card.dto.response.CardSaveResponse;
import com.practice.queenstrello.domain.card.dto.response.CardSimpleResponse;
import com.practice.queenstrello.domain.card.dto.response.CardUpdateResponse;
import com.practice.queenstrello.domain.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lists/{listId}/cards")
public class CardController {
    private final CardService cardService;

    //카드 생성
    @PostMapping
    public ResponseEntity<CardSaveResponse> saveCard(@RequestBody CardSaveRequest cardSaveRequest, @PathVariable long listId,  @PathVariable long workspaceId, @AuthenticationPrincipal AuthUser authUser){
        Long creatorId = authUser.getUserId(); //AuthUser에서 userId 가져옴
        CardSaveResponse cardSaveResponse = cardService.saveCard(cardSaveRequest,listId,creatorId,workspaceId);
        return ResponseEntity.ok(cardSaveResponse);
    }

    //카드 다건 조회
    @GetMapping
    public ResponseEntity<Page<CardSimpleResponse>> getCards(@PathVariable Long listId, @RequestParam Long memberId,@RequestParam Long workspaceId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<CardSimpleResponse> cardSimpleResponses = cardService.getCards(listId,memberId, workspaceId,page,size);
        return ResponseEntity.ok(cardSimpleResponses);
    }

    //카드 단건 조회
    @GetMapping("/{cardId}")
    public ResponseEntity<CardDetailResponse> getCard(@PathVariable long cardId,  @RequestParam Long memberId, @RequestParam Long workspaceId) {
        CardDetailResponse cardDetailResponse = cardService.getCard(cardId,memberId, workspaceId);
        return ResponseEntity.ok(cardDetailResponse);
    }

    //카드 수정
    @PutMapping("/{cardId}")
    public ResponseEntity<CardUpdateResponse> updateCard(@PathVariable Long cardId,@RequestBody CardUpdateRequest cardUpdateRequest, @RequestParam Long workspaceId, @AuthenticationPrincipal AuthUser authUser){
        Long userId = authUser.getUserId(); //AuthUser에서 userId 가져옴
        CardUpdateResponse updatedCard = cardService.updateCard(cardId,cardUpdateRequest,userId, workspaceId);
        return ResponseEntity.ok(updatedCard);
    }

    //카드 삭제
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId,@PathVariable Long listId,@PathVariable Long workspaceId,@AuthenticationPrincipal AuthUser authUser){
        Long userId = authUser.getUserId(); //AuthUser에서 userId 가져옴
        cardService.deleteCard(cardId,userId,workspaceId);
        return ResponseEntity.noContent().build();
    }




}
