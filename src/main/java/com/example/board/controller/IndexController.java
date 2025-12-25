package com.example.board.controller;

import com.example.board.dto.BoardResponseDto;
import com.example.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller // @RestController 아님! 화면을 반환할 때는 @Controller 사용
public class IndexController {

    private final BoardService boardService;

    @GetMapping("/") // 메인 페이지 매핑
    public String index(Model model) {
        // Service에서 가져온 글 목록을 'posts'라는 이름으로 model에 담아 View로 전달
        model.addAttribute("posts", boardService.findAllDesc());
        return "index"; // src/main/resources/templates/index.html을 찾아감
    }

    // ▼▼▼ 여기 추가하세요 ▼▼▼
    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save"; // posts-save.html을 호출
    }

    // 수정 화면 매핑
    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        BoardResponseDto dto = boardService.findById(id);
        model.addAttribute("post", dto); // 조회한 데이터를 'post'라는 이름으로 화면에 전달

        return "posts-update";
    }
}
