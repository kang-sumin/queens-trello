package com.practice.queenstrello.domain.card.service;

import com.practice.queenstrello.domain.card.dto.request.CardSaveRequest;
import com.practice.queenstrello.domain.card.dto.response.CardSaveResponse;
import com.practice.queenstrello.domain.card.entity.Card;
import com.practice.queenstrello.domain.card.entity.CardManager;
import com.practice.queenstrello.domain.card.repository.CardManagerRepository;
import com.practice.queenstrello.domain.card.repository.CardRepository;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.entity.UserRole;
import com.practice.queenstrello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final CardManagerRepository cardManagerRepository;
    private final UserRepository userRepository;

    @Transactional
    public CardSaveResponse saveCard(CardSaveRequest cardSaveRequest, long listId, Long creatorId) {
        //카드생성자 확인
        User creator = userRepository.findById(creatorId).orElseThrow(()->new IllegalArgumentException("유효하지 않는 user id 입니다. "));

        //읽기전용이면 예외처리
        if (creator.getUserRole().equals(UserRole.ROLE_USER)){
            throw new IllegalStateException("읽기 전용 사용자는 카드를 생성할 수 없습니다.");
        }

        //카드생성(생성자이용)
        Card card = new Card(cardSaveRequest.getTitle(),
                cardSaveRequest.getContent(),
                cardSaveRequest.getDeadLine());
        cardRepository.save(card);

        //담당자 추가 (생성자 써서 CardMAnager 생성/저장)
        List<User> managers = userRepository.findAllById(cardSaveRequest.getManagerIds());
        for (User manager : managers) {
            CardManager cardManager = new CardManager(card, manager);
            card.addCardManager(cardManager);
            cardManagerRepository.save(cardManager);
        }

        // ID로 담당자 리스트 반환
        List<Long> managerIds = managers.stream()
                .map(User::getId)
                .toList();

        return new CardSaveResponse(card.getId(),
                card.getTitle(),
                card.getContent(),
                card.getDeadLine(),
                managerIds);
    }
}
