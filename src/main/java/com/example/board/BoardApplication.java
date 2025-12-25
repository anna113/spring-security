package com.example.board;

import com.example.board.domain.entity.Member;
import com.example.board.domain.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableJpaAuditing
@SpringBootApplication
public class BoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

    // 서버 시작 시 실행되는 테스트 데이터 생성기
    @Bean
    public CommandLineRunner initData(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // "admin" / "1234" 계정이 없으면 생성
            if (memberRepository.findByUsername("admin").isEmpty()) {
                memberRepository.save(Member.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("1234")) // 비밀번호는 꼭 암호화해야 함!
                        .role("USER")
                        .build());
            }
        };
    }

}
