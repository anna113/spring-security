package com.example.board.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // 기본 생성자 자동 추가
@Entity // DB 테이블과 연결
public class Board extends BaseTimeEntity{

    @Id // PK (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder // 빌더 패턴 클래스 생성
    public Board(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    // 게시글 수정 (Setter 대신 명확한 메소드 사용)
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
