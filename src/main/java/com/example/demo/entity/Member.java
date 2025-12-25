package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity  // 1. 스프링아, 이거 DB 테이블로 만들어줘!
@Getter  // (Lombok) getter 자동 생성
@Setter  // (Lombok) setter 자동 생성
@NoArgsConstructor // (Lombok) 기본 생성자 자동 생성
public class Member {

    @Id // 2. 이게 PK(Primary Key, 주민등록번호 같은 것)야!
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 3. 번호는 1, 2, 3... 알아서 올려줘.
    private Long id;

    private String username; // 이름 컬럼
    private String email;    // 이메일 컬럼
}
