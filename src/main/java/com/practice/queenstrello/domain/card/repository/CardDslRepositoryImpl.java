package com.practice.queenstrello.domain.card.repository;

import com.practice.queenstrello.domain.card.entity.Card;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.practice.queenstrello.domain.card.entity.QCard.card;
import static com.practice.queenstrello.domain.card.entity.QCardManager.cardManager;

@Repository
@RequiredArgsConstructor
public class CardDslRepositoryImpl implements CardDslRepository {
    private final JPAQueryFactory queryFactory;


    //card 통합검색 QueryDsl
    @Override
    public Page<Card> searchCard(String title, String content, String deadline, String nickname, Pageable pageable) {
        List<Card> result  = queryFactory
                            .select(card)
                            .distinct()
                            .from(card)
                            .leftJoin(card.cardManagers, cardManager).fetchJoin()
                            .where(titleContains(title),
                                    contentContains(content),
                                    deadLineEq(deadline),
                                    managerEq(nickname)
                            ).offset(pageable.getOffset())
                            .limit(pageable.getPageSize())
                            .fetch();
        Long totalCount = (long) result.size();
        return new PageImpl<>(result, pageable, totalCount);
    }

    //null이 아니면 제목에 해당 글자가 포함된 경우
    private BooleanExpression titleContains(String title) {
        return title != null ? card.title.contains(title) : null;
    }

    //null이 아니면 내용에 해당 글자가 포함된 경우
    private BooleanExpression contentContains(String content) {
        return content != null ? card.content.contains(content) : null;
    }

    //null아니면 해당 deadline의 날짜를 가진 경우
    private BooleanExpression deadLineEq(String deadline) {
        return deadline !=null ? Expressions.dateTemplate(LocalDate.class, "DATE({0})", card.deadLine)
                .eq(LocalDate.parse(deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd"))) : null;
    }

    //card 담당자중에 해당 manager가 있는 경우
    private BooleanExpression managerEq(String nickname) {
        return nickname != null ? cardManager.manager.nickname.eq(nickname) : null;
    }
}
