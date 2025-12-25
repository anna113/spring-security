package com.example.board.config;

import com.example.board.jwt.JwtAccessDeniedHandler;
import com.example.board.jwt.JwtAuthenticationEntryPoint;
import com.example.board.jwt.JwtAuthenticationFilter;
import com.example.board.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // [PART 1] 기본 설정 비활성화
                .httpBasic((basic) -> basic.disable()) // UI를 사용하는 기본 인증 비활성화 (API만 사용하므로)
                .csrf((csrf) -> csrf.disable()) // CSRF 보안 비활성화 (API 서버라 필요 없음)

                // [PART 2] 세션 미사용 설정 (가장 중요!)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // [예외 처리 설정 추가]
                .exceptionHandling((exception) -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 401 에러 핸들링
                        .accessDeniedHandler(jwtAccessDeniedHandler)         // 403 에러 핸들링
                )

                // [PART 3] 기존의 화면 깨짐 방지 설정 유지
                .headers((headers) -> headers
                        .frameOptions((frame) -> frame.sameOrigin())
                )

                // [PART 4] URL 별 권한 관리
                .authorizeHttpRequests((auth) -> auth
                        // ▼▼▼ 여기 경로를 잘 봐주세요! (/members/...) ▼▼▼
                        .requestMatchers("/members/login", "/members/join").permitAll()
                        .requestMatchers("/members/test").hasRole("USER")
                        .anyRequest().authenticated()
                )

                // [PART 5] JWT 필터 등록
                // 기존의 UsernamePasswordAuthenticationFilter 앞에 우리가 만든 필터를 끼워 넣음
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        // ▼ formLogin(), logout() 설정은 이제 필요 없어서 삭제했습니다! ▼

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}