package com.example.board.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtToken {
    private String grantType;   // JWT 권한 인증 타입 (Bearer 등)
    private String accessToken; // 우리가 만든 진짜 토큰
    private String refreshToken; // (추후 구현 예정)
}