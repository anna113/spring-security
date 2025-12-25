package com.example.board.domain.repository;

import com.example.board.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// <Entity 클래스, PK 타입> 상속 시 기본 CRUD 메소드 자동 생성
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByIdDesc();
}


