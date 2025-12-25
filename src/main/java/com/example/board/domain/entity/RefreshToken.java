package com.example.board.domain.entity;

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
    private String key; // 사용자의 ID (username)

    private String value; // Refresh Token String

    // 토큰 갱신 시 내용 변경을 위한 메서드
    public void updateValue(String token) {
        this.value = token;
    }
}