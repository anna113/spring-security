package com.example.demo.repository;

import com.example.demo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<다룰 대상, ID의 타입> 상속받기
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 끝입니다. 진짜로요.
}
