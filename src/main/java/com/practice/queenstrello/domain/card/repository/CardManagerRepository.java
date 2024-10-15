package com.practice.queenstrello.domain.card.repository;

import com.practice.queenstrello.domain.card.entity.CardManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardManagerRepository extends JpaRepository<CardManager,Long> {
}
