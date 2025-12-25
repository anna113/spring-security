package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // 1. 여기는 웨이터(컨트롤러)입니다! 라고 명찰 달기
public class HelloController {

    @GetMapping("/hello") // 2. "/hello" 라는 주소로 요청이 오면 이 메소드 실행!
    public String sayHello() {
        return "안녕하세요! 스프링 부트 세상에 오신 걸 환영합니다! 🎉";
    }
}
