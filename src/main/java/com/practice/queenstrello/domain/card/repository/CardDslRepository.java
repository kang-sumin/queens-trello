package com.practice.queenstrello.domain.card.repository;

import com.practice.queenstrello.domain.card.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardDslRepository {
    //card 통합검색 QueryDsl
    Page<Card> searchCard(String title, String content, String deadline, String nickname, Pageable pageable);
}
