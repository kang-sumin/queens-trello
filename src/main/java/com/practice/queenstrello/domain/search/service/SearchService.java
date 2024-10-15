package com.practice.queenstrello.domain.search.service;

import com.practice.queenstrello.domain.search.dto.SearchResponse;
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

    //카드 통랍검색
    public Page<SearchResponse> searchCard(int page, int size, String title, String content, String deadline, String nickname) {
        //기본값으로 manager는 null
        User manager = null;
        //닉네임 있으면 해당 닉네임을 갖는 manager를 찾기
        if (nickname !=null) manager = userRepository.findByNickname(nickname).orElseThrow(()-> new NoNicnameUserException(nickname));
        //pageable만들기
        Pageable pageable = PageRequest.of(page-1, size);
        //통합검색 -> responseDto형태로 변환후 내보내기
        return cardRepository.searchCard(title, content, deadline, manager, pageable)
                .map(card -> new SearchResponse(card.getTitle(),card.getContent(),card.getDeadLine(),card.getCardManagers()));
    }
}
