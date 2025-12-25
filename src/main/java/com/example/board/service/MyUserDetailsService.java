package com.example.board.service;

import com.example.board.domain.entity.Member;
import com.example.board.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. DB에서 username으로 회원 조회
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다."));

        // 2. 시큐리티가 이해할 수 있는 UserDetails 객체로 변환해서 반환
        // (User 클래스는 스프링 시큐리티가 제공하는 기본 UserDetails 구현체입니다)
        return User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .roles(member.getRole()) // "USER" -> "ROLE_USER" 자동 변환됨
                .build();
    }
}
