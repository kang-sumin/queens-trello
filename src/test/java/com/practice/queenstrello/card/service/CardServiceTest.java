package com.practice.queenstrello.card.service;

import com.practice.queenstrello.domain.card.dto.request.CardSaveRequest;
import com.practice.queenstrello.domain.card.dto.response.CardSaveResponse;
import com.practice.queenstrello.domain.card.entity.Card;
import com.practice.queenstrello.domain.card.repository.CardManagerRepository;
import com.practice.queenstrello.domain.card.repository.CardRepository;
import com.practice.queenstrello.domain.card.service.CardService;
import com.practice.queenstrello.domain.list.entity.BoardList;
import com.practice.queenstrello.domain.list.repository.BoardListRepository;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.entity.UserRole;
import com.practice.queenstrello.domain.user.repository.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    @InjectMocks
    private CardService cardService;

    @Test
    void save_card_성공() {
        //Given
        Long creatorId = 1L;
        Long listId = 1L;
        CardSaveRequest request = new CardSaveRequest("title","content", LocalDateTime.now().plusDays(1), List.of(2L,3L));

        User creator = new User();
        creator.setId(creatorId);
        creator.setNickname("master");
        creator.setUserRole(UserRole.ROLE_MASTER);

        BoardList boardList = new BoardList();
        boardList.setId(listId);
        boardList.setTitle("title");

        Card card = new Card();
        card.setTitle(request.getTitle());
        card.setContent(request.getContent());
        card.setDeadLine(request.getDeadLine());
        card.setBoardList(boardList);

        User manager1 = new User();
        manager1.setId(2L);
        manager1.setNickname("Manager 1");

        User manager2 = new User();
        manager2.setId(3L);
        manager2.setNickname("Manager 2");

        when(userRepository.findById(creatorId)).thenReturn(Optional.of(creator));
        when(boardListRepository.findById(listId)).thenReturn(Optional.of(boardList));
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        when(userRepository.findAllById(request.getManagerIds())).thenReturn(List.of(manager1, manager2));

        //when
        CardSaveResponse response = cardService.saveCard(request, listId, creatorId);

        // Then
        assertNotNull(response);
        assertEquals(card.getId(), response.getCardId());
        assertEquals(request.getTitle(), response.getTitle());
        assertEquals(request.getContent(), response.getContent());
        assertEquals(request.getDeadLine(), response.getDeadline());
        assertEquals(List.of(2L, 3L), response.getManagerIds());
    }





}
