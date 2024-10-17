package com.practice.queenstrello.domain.card.repository;

import com.practice.queenstrello.domain.card.entity.CardLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardLogRepository extends JpaRepository<CardLog,Long> {
}
