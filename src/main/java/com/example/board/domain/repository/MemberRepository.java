package com.example.board.domain.repository;

import com.example.board.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 아이디(username)로 회원 정보 조회
    Optional<Member> findByUsername(String username);
}
