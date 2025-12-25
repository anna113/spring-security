package com.example.board.service;

import com.example.board.domain.entity.Member;
import com.example.board.domain.entity.RefreshToken;
import com.example.board.domain.repository.MemberRepository;
import com.example.board.domain.repository.RefreshTokenRepository;
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
    private final RefreshTokenRepository refreshTokenRepository; // 추가됨

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

    @Transactional
    public JwtToken login(String username, String password) {
        // 1. ID/PW 검증
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 2. Access + Refresh Token 생성
        JwtToken jwtToken = jwtTokenProvider.createToken(authentication);

        // 3. Refresh Token 저장 (기존에 있으면 업데이트, 없으면 저장)
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(authentication.getName())
                .refreshToken(jwtToken.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken); // 저장

        return jwtToken;
    }

    // ★ 토큰 재발급(Reissue) 로직
    @Transactional
    public JwtToken reissue(String refreshToken) {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. 토큰에서 User ID 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

        // 3. 저장소에서 User ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken dbRefreshToken = refreshTokenRepository.findByUserId(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. 토큰 일치 여부 검사 (핵심!)
        if (!dbRefreshToken.getRefreshToken().equals(refreshToken)) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        JwtToken newJwtToken = jwtTokenProvider.createToken(authentication);

        // 6. 저장소 정보 업데이트 (Rotation)
        dbRefreshToken.updateRefreshToken(newJwtToken.getRefreshToken());

        return newJwtToken;
    }
}