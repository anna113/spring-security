package com.example.board.service;

import com.example.board.domain.entity.Member;
import com.example.board.domain.repository.MemberRepository;
import com.example.board.dto.MemberJoinDto;
import com.example.board.jwt.JwtToken;
import com.example.board.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder; // SecurityConfig에서 등록한 그 녀석!

    public void join(MemberJoinDto dto) {
        // 1. 같은 아이디가 있는지 중복 체크 (선택 사항이지만 필수 권장)
        if (memberRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }

        // 2. 비밀번호 암호화
        // 사용자가 입력한 "1234"를 "$2a$10$..." 같은 알 수 없는 문자로 변환합니다.
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // 3. 회원 저장
        Member member = Member.builder()
                .username(dto.getUsername())
                .password(encodedPassword) // 암호화된 비번 저장
                .role("USER") // 기본 권한은 USER
                .build();

        memberRepository.save(member);
    }

    // ★ 실제 로그인 처리 로직
    @Transactional
    public JwtToken login(String username, String password) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.createToken(authentication);

        return jwtToken;
    }
}