package com.practice.queenstrello.domain.card.service;

import com.practice.queenstrello.domain.card.dto.request.CardSaveRequest;
import com.practice.queenstrello.domain.card.dto.request.CardUpdateRequest;
import com.practice.queenstrello.domain.card.dto.response.CardDetailResponse;
import com.practice.queenstrello.domain.card.dto.response.CardSaveResponse;
import com.practice.queenstrello.domain.card.dto.response.CardSimpleResponse;
import com.practice.queenstrello.domain.card.dto.response.CardUpdateResponse;
import com.practice.queenstrello.domain.card.entity.Card;
import com.practice.queenstrello.domain.card.entity.CardManager;
import com.practice.queenstrello.domain.card.repository.CardManagerRepository;
import com.practice.queenstrello.domain.card.repository.CardRepository;
import com.practice.queenstrello.domain.comment.dto.response.CommentSaveResponse;
import com.practice.queenstrello.domain.comment.repository.CommentRepository;
import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.list.entity.BoardList;
import com.practice.queenstrello.domain.list.repository.BoardListRepository;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.entity.UserRole;
import com.practice.queenstrello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {
    private final CardRepository cardRepository;
    private final CardLogService cardLogService;
    private final CardManagerRepository cardManagerRepository;
    private final UserRepository userRepository;
    private final BoardListRepository boardListRepository;
    private final CommentRepository commentRepository;

    //카드 생성
    @Transactional
    public CardSaveResponse saveCard(CardSaveRequest cardSaveRequest, long listId, Long creatorId) {
        //카드생성자 확인
        User creator = userRepository.findById(creatorId).orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_USER));

        //읽기전용이면 예외처리
        if (creator.getUserRole().equals(UserRole.ROLE_USER)) {
            throw new QueensTrelloException(ErrorCode.INVALID_USERROLE);
        }

        //리스트 확인
        BoardList boardList = boardListRepository.findById(listId).orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_LIST));


        //카드생성(리스트랑 연결)
        Card card = new Card(cardSaveRequest.getTitle(),
                cardSaveRequest.getContent(),
                cardSaveRequest.getDeadLine(),
                boardList); //리스트랑 연결
        boardList.addCard(card); //리스트에 카드 추가

        cardRepository.save(card);

        //담당자 추가 (생성자 써서 CardManager 생성/저장)
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

    //카드 다건 조회
    public Page<CardSimpleResponse> getCards(Long listId, int page, int size) {
        Pageable pagealbe = PageRequest.of(page, size);
        Page<Card> cards = cardRepository.findByListIdWithManagers(listId, pagealbe);

        return cards.map(card -> new CardSimpleResponse(
                card.getTitle(),
                card.getContent(),
                card.getDeadLine(),
                card.getCreatedAt(),
                card.getCardManagers().stream()
                        .map(cardManager -> cardManager.getManager().getId())
                        .toList()
        ));
    }

    //카드 단건(상세) 조회
    public CardDetailResponse getCard(long cardId) {
        //카드정보 조회
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new QueensTrelloException((ErrorCode.INVALID_CARD)));

        //카드 정보와 담당자 목록 리스폰스하기
        return new CardDetailResponse(
                card.getTitle(),
                card.getContent(),
                card.getDeadLine(),
                card.getCardManagers().stream()
                        .map(cardManager -> cardManager.getManager().getId())
                        .toList(),
                card.getComments().stream()
                        .map(comment -> new CommentSaveResponse(comment.getId(), comment.getContent(), comment.getUser().getId(), comment.getCreatedAt()))
                        .toList()
        );
    }

    //카드 수정
    @Transactional
    public CardUpdateResponse updateCard(Long cardId, CardUpdateRequest cardUpdateRequest, Long userId) {
        //수정하려는 사용자 권한 먼저 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_USER));

        //읽기전용인 사람은 예외처리
        if (user.getUserRole().equals(UserRole.ROLE_USER)) {
            throw new QueensTrelloException(ErrorCode.INVALID_USERROLE);
        }

        //카드 찾기
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_CARD));

        //현재 시간 기록(언제 수정했는지 기록하기 위해서)
        LocalDateTime updateTime = LocalDateTime.now();
        //로그 메시지 생성
        String logMessage = String.format("User %s updated card %s : title='%s', content='%s', deadline='%s'",
                user.getId(), cardId, updateTime,cardUpdateRequest.getTitle(), cardUpdateRequest.getContent(), cardUpdateRequest.getDeadLine());
        //로그 저장(로그 저장이 실패해도 수정 작업에 영향을 주지 않도록 트라이캐치)
        try {
            cardLogService.saveLog(user,card,logMessage);
        } catch (Exception e) {
            //로그 저장 중 예외 발생해도, 로그만 별도 트랜잭션으로 구현했으니 수정과 별개로 처리해야한다.
            System.out.println("로그 저장 중 에러가 발생했습니다." + e.getMessage());
        }


        //카드 수정(제목,설명,데드라인)->메서드만들어서 사용함
        card.updateCard(cardUpdateRequest.getTitle(), cardUpdateRequest.getContent(), cardUpdateRequest.getDeadLine());

        //*** 담당자 수정 과정
        // 1. 기존 담당자 리스트 조회
        List<CardManager> existingManagers = cardManagerRepository.findByCardId(cardId);
        // 2. 요청에서 들어온 담당자 ID 리스트
        List<Long> newManagerIds = cardUpdateRequest.getManagerIds();
        // 3. 기존 담당자 중에서 제거할 담당자들 삭제
        for (CardManager existingManager : existingManagers) {
            if (!newManagerIds.contains(existingManager.getManager().getId())) {
                cardManagerRepository.deleteByCardIdAndManagerId(cardId, existingManager.getManager().getId());
            }
        }
        // 4. 새로 추가할 담당자 추가
        List<User> newManagers = userRepository.findAllById(newManagerIds);
        for (User manager : newManagers) {
            //이미 등록된 담당자가 아닐 때 추가하게끔 조건 걸기
            if (existingManagers.stream().noneMatch(cardManager -> cardManager.getManager().getId().equals(manager.getId()))) {
                CardManager newCardManager = new CardManager(card, manager);
                card.addCardManager(newCardManager);
                cardManagerRepository.save(newCardManager);
            }
        }

        //5. 수정된 담당자 리스트 반환
        List<Long> updatedManagerIds = newManagers.stream()
                .map(User::getId)
                .toList();


        return new CardUpdateResponse(card.getId(),
                card.getTitle(),
                card.getContent(),
                card.getDeadLine(),
                updatedManagerIds);

    }

    //카드 삭제
    @Transactional
    public void deleteCard(Long cardId, Long userId) {
        //유저가 존재하는지 먼저 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_USER));
        //삭제하려는 카드 조회
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_CARD));
        //읽기전용 사용자는 카드 삭제 불가
        if (user.getUserRole().equals(UserRole.ROLE_USER)) {
            throw new QueensTrelloException(ErrorCode.INVALID_USERROLE);
        }
        //카드에 연결된 데이터(카드 매니저, 댓글..) 삭제
        cardManagerRepository.deleteByCardId(cardId); //담당자 삭제
        commentRepository.deleteByCardId(cardId); //댓글 삭제

        //카드 삭제
        cardRepository.delete(card);
    }

}
