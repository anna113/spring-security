package com.example.board.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    // 1. 누구나 접근 가능 (SecurityConfig에서 "/test"를 permitAll 함)
    @GetMapping("/test")
    public String test() {
        return "<h1>Hello Security!</h1> <br> 여기는 로그인 없이도 들어올 수 있습니다.";
    }

    // 2. 로그인한 사람만 접근 가능 (anyRequest().authenticated()에 걸림)
    @GetMapping("/test/user")
    public String user() {
        return "<h1>Welcome User!</h1> <br> 로그인 성공하셨군요!";
    }
}