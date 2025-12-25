package com.example.board.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 1. 상속받는 자식 클래스에게 매핑 정보(속성)만 제공
@EntityListeners(AuditingEntityListener.class) // 2. Auditing(자동 값 매핑) 기능 포함
public abstract class BaseTimeEntity {

    @CreatedDate // 생성될 때 시간 자동 저장
    @Column(updatable = false) // 수정 시에는 관여 안 함
    private LocalDateTime createdDate;

    @LastModifiedDate // 조회한 Entity의 값을 변경할 때 시간 자동 저장
    private LocalDateTime modifiedDate;
}
