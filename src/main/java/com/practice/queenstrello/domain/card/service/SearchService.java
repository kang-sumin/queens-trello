package com.practice.queenstrello.domain.card.service;

import com.practice.queenstrello.domain.card.dto.SearchResponse;
import com.practice.queenstrello.domain.card.repository.CardRepository;
import com.practice.queenstrello.domain.common.exception.NoNicnameUserException;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public Page<SearchResponse> searchCard(int page, int size, String title, String content, String deadline, String nickname) {
        User manager = null;
        if (nickname !=null) manager = userRepository.findByNickname(nickname).orElseThrow(()-> new NoNicnameUserException(nickname));
        Pageable pageable = PageRequest.of(page-1, size);
        return cardRepository.searchCard(title, content, deadline, manager, pageable)
                .map(card -> new SearchResponse(card.getTitle(),card.getContent(),card.getDeadLine(),card.getCardManagers()));
    }
}
