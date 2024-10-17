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
import com.practice.queenstrello.domain.user.repository.UserRepository;
import com.practice.queenstrello.domain.workspace.entity.MemberRole;
import com.practice.queenstrello.domain.workspace.repository.WorkspaceMemberRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
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
    private final WorkspaceMemberRepository workspaceMemberRepository;

    //카드 생성
    @Transactional
    public CardSaveResponse saveCard(CardSaveRequest cardSaveRequest, long listId, Long creatorId, Long workspaceId) {

        //워크스페이스에 속해 있는 멤버인지 확인
        boolean isMember = workspaceMemberRepository.existsByMemberIdAndWorkspaceId(creatorId, workspaceId);
        if (!isMember) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION);
        }
        // 워크스페이스 내에서 유저의 권한을 확인 ->READ만 아니면 된다. READ 상위애들 ROLE들은 다 할 수 있으니까.
        boolean isReadOnly = workspaceMemberRepository.existsByMemberIdAndWorkspaceIdAndMemberRole(creatorId, workspaceId, MemberRole.READ);
        if (isReadOnly) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION);
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
        } //saveall메소드 고려

        // ID로 담당자 리스트 반환 -> 첨부내용 카드에는 첨부파일이 들어간다. 로그기록 기능을 ^고려^해보자 시러요 ㅜ
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
    public Page<CardSimpleResponse> getCards(Long listId, Long memberId, Long workspaceId, int page, int size) {
        //워크스페이스에 속해 있는 멤버인지 확인
        boolean isMember = workspaceMemberRepository.existsByMemberIdAndWorkspaceId(memberId, workspaceId);
        if (!isMember) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION);
        }

        Pageable pagealbe = PageRequest.of(page, size);
        Page<Card> cards = cardRepository.findByListIdWithManagers(listId, pagealbe); //한번 빼고 되나

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
    public CardDetailResponse getCard(long cardId, Long memberId, Long workspaceId) {
        // 워크스페이스에 속해 있는 멤버인지 확인
        boolean isMember = workspaceMemberRepository.existsByMemberIdAndWorkspaceId(memberId, workspaceId);
        if (!isMember) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION);
        }

        //카드정보 조회
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new QueensTrelloException((ErrorCode.INVALID_CARD)));

        //카드 정보와 담당자 목록 리스폰스하기 + 댓글도 -> +첨부파일
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
        ); //of 나 builder 쓰면 간결해짐
    }

    //카드 수정 + 동시성 제어
    @Transactional
    public CardUpdateResponse updateCard(Long cardId, CardUpdateRequest cardUpdateRequest, Long userId, Long workspaceId) {

        //사용자 확인->로그에 써야함
        User user = userRepository.findById(userId).orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_USER));

        // 워크스페이스에 속해 있는 멤버인지 확인
        boolean isMember = workspaceMemberRepository.existsByMemberIdAndWorkspaceId(userId, workspaceId);
        if (!isMember) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION);
        }

        // 워크스페이스 내에서 유저의 권한을 확인 -> READ가 아닌지 확인
        boolean isReadOnly = workspaceMemberRepository.existsByMemberIdAndWorkspaceIdAndMemberRole(userId, workspaceId, MemberRole.READ);
        if (isReadOnly) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION);
        }

        //충돌 났을 때 재시도 로직..최대 3번까지만 리트라이 가능
        int maxRetries = 3;
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                // **카드 업데이트 로직 부분
                //카드 찾기
                Card card = cardRepository.findById(cardId).orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_CARD));
                //현재 시간 기록(언제 수정했는지 기록하기 위해서)
                LocalDateTime updateTime = LocalDateTime.now();
                //로그 메시지 생성
                String logMessage = String.format("User %s updated card %s : title='%s', content='%s', deadline='%s'",
                        user.getId(), cardId, updateTime, cardUpdateRequest.getTitle(), cardUpdateRequest.getContent(), cardUpdateRequest.getDeadLine());
                //로그 저장(로그 저장이 실패해도 수정 작업에 영향을 주지 않도록 트라이캐치)
                try {
                    cardLogService.saveLog(user, card, logMessage);
                } catch (Exception e) {
                    //로그 저장 중 예외 발생해도, 로그만 별도 트랜잭션으로 구현했으니 수정과 별개로 처리해야한다.
                    log.error("로그 저장 중 에러가 발생했습니다." + e.getMessage());
                }
                //카드 수정(제목,설명,데드라인) -> 메서드만들어서 사용함
                card.updateCard(cardUpdateRequest.getTitle(), cardUpdateRequest.getContent(), cardUpdateRequest.getDeadLine());

                //*** 담당자 수정 과정
                // 1. 기존 담당자 리스트 조회
                List<CardManager> existingManagers = cardManagerRepository.findByCardId(cardId);
                // 2. 요청에서 들어온 담당자 ID 리스트
                List<Long> newManagerIds = cardUpdateRequest.getManagerIds();
                // 3. 기존 담당자 중에서 제거할 담당자들 삭제
                Set<Long> existingManagerIds = existingManagers.stream()
                        .map(manager -> manager.getManager().getId())
                        .collect(Collectors.toSet());

                Set<Long> newManagerIdSet = new HashSet<>(newManagerIds);
                // 기존 담당자 중에서 새로운 리스트에 없는 담당자를 삭제
                for (CardManager existingManager : existingManagers) {
                    if (!newManagerIdSet.contains(existingManager.getManager().getId())) {
                        cardManagerRepository.deleteByCardIdAndManagerId(cardId, existingManager.getManager().getId());
                    }
                }
                // 4. 새로 추가할 담당자 추가
                List<User> newManagers = userRepository.findAllById(newManagerIds);
                // 새로운 담당자 중 기존에 없는 담당자만 추가
                List<CardManager> managersToAdd = newManagers.stream()
                        .filter(manager -> !existingManagerIds.contains(manager.getId()))
                        .map(manager -> new CardManager(card, manager))
                        .collect(Collectors.toList());

                if (!managersToAdd.isEmpty()) {
                    cardManagerRepository.saveAll(managersToAdd);
                }

                //5. 수정된 담당자 리스트 반환
                List<Long> updatedManagerIds = newManagers.stream()
                        .map(User::getId)
                        .collect(Collectors.toList());

                cardRepository.save(card); //변경사항 저장


                return new CardUpdateResponse(card.getId(),
                        card.getTitle(),
                        card.getContent(),
                        card.getDeadLine(),
                        updatedManagerIds);

            } catch (OptimisticLockException e) {
                // 예외처리 및 재시도 로직
                if (retryCount == maxRetries - 1) { //재시도 카운트가 마지막 재시도일 때 예외던지려고.
                    throw new QueensTrelloException(ErrorCode.CONCURRENT_UPDATE_ERROR);
                }
                retryCount++;
                try {
                    Thread.sleep(100); //잠깐 대기해라요이우잉잉
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new QueensTrelloException(ErrorCode.CONCURRENT_UPDATE_ERROR);
                }
            }
        }
        throw new QueensTrelloException(ErrorCode.CONCURRENT_UPDATE_ERROR);
    }

    //카드 삭제
    @Transactional
    public void deleteCard(Long cardId, Long userId, Long workspaceId) {
        int maxRetries = 3;
        int retryCount = 0;

        while(retryCount<maxRetries){
            try {
                //삭제하려는 카드 조회
                Card card = cardRepository.findById(cardId).orElseThrow(() -> new QueensTrelloException(ErrorCode.INVALID_CARD));
                // 워크스페이스에 속해 있는 멤버인지 확인
                boolean isMember = workspaceMemberRepository.existsByMemberIdAndWorkspaceId(userId, workspaceId);
                if (!isMember) {
                    throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION);
                }

                // 워크스페이스 내에서 유저의 권한을 확인 -> READ만 아니면 된다.
                boolean isReadOnly = workspaceMemberRepository.existsByMemberIdAndWorkspaceIdAndMemberRole(userId, workspaceId, MemberRole.READ);
                if (isReadOnly) {
                    throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION);
                }
                //카드에 연결된 데이터(카드 매니저, 댓글..) 삭제 ->cascade ,카드 날리기 전에 s3에 직접 첨부파일 삭제요청을 날리는 메서드
                cardManagerRepository.deleteByCardId(cardId); //담당자 삭제
                commentRepository.deleteByCardId(cardId); //댓글 삭제

                //카드 삭제
                cardRepository.delete(card);

                return;
            } catch (OptimisticLockException e){
                if(retryCount == maxRetries - 1) {
                    //최대 재시도 횟수 초과하면 예외
                    throw new QueensTrelloException(ErrorCode.CONCURRENT_DELETE_ERROR);
                }
                retryCount++;
                try {
                    //재시도 전 잠시 대기
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new QueensTrelloException(ErrorCode.CONCURRENT_DELETE_ERROR);
                }
            }
        }

        //모든 재시도 실패 시
        throw new QueensTrelloException(ErrorCode.CONCURRENT_DELETE_ERROR);
    }

}
