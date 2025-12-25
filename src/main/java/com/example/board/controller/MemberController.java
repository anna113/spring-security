package com.example.board.controller;

import com.example.board.dto.MemberJoinDto;
import com.example.board.dto.MemberLoginDto;
import com.example.board.jwt.JwtToken;
import com.example.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    // 1. 로그인 API
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
    @PostMapping("/test")
    public String test() {
        return "success";
    }
}