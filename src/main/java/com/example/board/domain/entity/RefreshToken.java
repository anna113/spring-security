package com.example.board.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @Column(name = "user_id") // DB 컬럼명을 user_id로 지정
    private String userId; // 사용자의 ID (username)

    @Column(name = "refresh_token")
    private String refreshToken;

    // 토큰 갱신 시 내용 변경을 위한 메서드
    public void updateRefreshToken(String token) {
        this.refreshToken = token;
    }
}