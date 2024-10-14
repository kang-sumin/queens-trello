package com.practice.queenstrello.domain.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 생성일만 포함된 클래스
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class CreatedTimestamped {
    // 생성일
    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
}
