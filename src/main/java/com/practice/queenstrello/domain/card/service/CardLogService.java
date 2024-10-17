package com.practice.queenstrello.domain.card.service;

import com.practice.queenstrello.domain.card.entity.Card;
import com.practice.queenstrello.domain.card.entity.CardLog;
import com.practice.queenstrello.domain.card.repository.CardLogRepository;
import com.practice.queenstrello.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardLogService {

    private final CardLogRepository cardLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW) //독립적인 트랜잭션
    public void saveLog(Long user, Long card, String logMessage) {
        CardLog log = new CardLog(user,card,logMessage);
        cardLogRepository.save(log);
    }
}
