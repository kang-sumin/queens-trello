package com.practice.queenstrello.card.service;

import com.practice.queenstrello.domain.card.dto.request.CardSaveRequest;
import com.practice.queenstrello.domain.card.dto.response.CardSaveResponse;
import com.practice.queenstrello.domain.card.entity.Card;
import com.practice.queenstrello.domain.card.entity.CardManager;
import com.practice.queenstrello.domain.card.repository.CardManagerRepository;
import com.practice.queenstrello.domain.card.repository.CardRepository;
import com.practice.queenstrello.domain.card.service.CardService;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.list.entity.BoardList;
import com.practice.queenstrello.domain.list.repository.BoardListRepository;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.entity.UserRole;
import com.practice.queenstrello.domain.user.repository.UserRepository;
import com.practice.queenstrello.domain.workspace.entity.MemberRole;
import com.practice.queenstrello.domain.workspace.repository.WorkspaceMemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BoardListRepository boardListRepository;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardManagerRepository cardManagerRepository;
    @Mock
    private WorkspaceMemberRepository workspaceMemberRepository;

    @InjectMocks
    private CardService cardService;
//    public CardServiceTest(){
//        //MockitoAnnotations.openMocks(this);
//    }

    @Test
    void save_card_성공() {
        //Given
        long listId = 1L;
        long creatorId = 1L;
        long workspaceId=1L;

        CardSaveRequest cardSaveRequest = new CardSaveRequest("한강 수온","몇도냐", LocalDateTime.now(), List.of(1L,2L));

        BoardList boardList = mock(BoardList.class);
        Card card = mock(Card.class);
        User manager1 = mock(User.class);
        User manager2 = mock(User.class);

        when(workspaceMemberRepository.existsByMemberIdAndWorkspaceId(creatorId, workspaceId)).thenReturn(true);
        System.out.println("----");
        System.out.println("isMember: " + workspaceMemberRepository.existsByMemberIdAndWorkspaceId(creatorId, workspaceId)); // 디버깅
        when(workspaceMemberRepository.existsByMemberIdAndWorkspaceIdAndMemberRole(creatorId, workspaceId, MemberRole.READ)).thenReturn(false);
        when(boardListRepository.findById(listId)).thenReturn(Optional.of(boardList));
        when(userRepository.findAllById(cardSaveRequest.getManagerIds())).thenReturn(List.of(manager1,manager2));
        when(cardRepository.save(any(Card.class))).thenReturn(card);


        //When
        CardSaveResponse response = cardService.saveCard(cardSaveRequest,listId,creatorId,workspaceId);

        //then
        verify(cardRepository, times(1)).save(any(Card.class));
        verify(cardManagerRepository, times(2)).save(any(CardManager.class));
        verify(boardList, times(1)).addCard(any(Card.class));

        // additional assertions on the response
        assert response != null;
        assert response.getTitle().equals(cardSaveRequest.getTitle());
        assert response.getManagerIds().size() == 2;


    }

    @Test
    public void 워크스페이스멤버_아니면_실패(){
        //given
        long listId = 1L;
        long creatorId = 1L;
        long workspaceId = 1L;

        CardSaveRequest cardSaveRequest = new CardSaveRequest("하루만","더줘",LocalDateTime.now(),List.of(1L,2L));

        when(workspaceMemberRepository.existsByMemberIdAndWorkspaceId(creatorId,workspaceId)).thenReturn(false);

        //when & then
        QueensTrelloException exception = assertThrows(QueensTrelloException.class,()->{cardService.saveCard(cardSaveRequest,listId,creatorId,workspaceId);});
        System.out.println("예외발생 - " + exception.getMessage());

        //verify(cardRepository,never()).save(any(Card.class));

    }

}
