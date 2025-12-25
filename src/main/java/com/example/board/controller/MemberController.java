package com.example.board.controller;

import com.example.board.dto.MemberJoinDto;
import com.example.board.dto.MemberLoginDto;
import com.example.board.jwt.JwtToken;
import com.example.board.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 관련 API (로그인, 가입, 재발급)") // ★ 컨트롤러 설명
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    // 1. 로그인 API
    @Operation(summary = "로그인", description = "ID와 PW를 이용하여 Access Token, Refresh Token을 발급합니다.") // ★ 메서드 설명
    @PostMapping("/login")
    public JwtToken login(@RequestBody MemberLoginDto memberLoginDto) {
        String username = memberLoginDto.getUsername();
        String password = memberLoginDto.getPassword();
        JwtToken jwtToken = memberService.login(username, password);
        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }

    // 2. 회원가입 API
    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    @PostMapping("/join")
    public String join(@RequestBody MemberJoinDto memberJoinDto) {
        try {
            memberService.join(memberJoinDto);
            return "회원가입 성공";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // 3. 테스트 API (로그인 한 사람만 접근 가능)
    @Operation(summary = "테스트", description = "테스트입니다.")
    @PostMapping("/test")
    public String test() {
        return "success";
    }

    @Operation(summary = "토큰 재발급", description = "토큰을 재발급합니다.")
    @PostMapping("/reissue")
    public JwtToken reissue(@RequestBody JwtToken jwtToken) {
        // refreshToken만 보내도 되지만, DTO 재활용을 위해 전체를 받음
        return memberService.reissue(jwtToken.getRefreshToken());
    }
}