package com.example.board.service;

import com.example.board.domain.entity.Board;
import com.example.board.domain.repository.BoardRepository;
import com.example.board.dto.BoardResponseDto;
import com.example.board.dto.BoardSaveRequestDto;
import com.example.board.dto.BoardUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    // 1. 게시글 저장
    @Transactional
    public Long save(BoardSaveRequestDto requestDto) {
        return boardRepository.save(requestDto.toEntity()).getId();
    }

    // 2. 게시글 수정
    @Transactional
    public Long update(Long id, BoardUpdateRequestDto requestDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        // JPA의 영속성 컨텍스트 덕분에, 값만 변경하면 자동으로 update 쿼리가 실행됩니다.
        board.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    // 3. 게시글 조회
    public BoardResponseDto findById(Long id) {
        Board entity = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new BoardResponseDto(entity);
    }

    // ▼▼ 전체 조회 메소드 추가 ▼▼
    @Transactional(readOnly = true) // 조회 기능이므로 읽기 전용으로 설정 (성능 향상)
    public List<BoardResponseDto> findAllDesc() {
        return boardRepository.findAllByOrderByIdDesc().stream()
                .map(BoardResponseDto::new) // Board -> BoardResponseDto 변환
                .collect(Collectors.toList());
    }

    // 삭제 기능 추가
    @Transactional
    public void delete(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        boardRepository.delete(board); // JpaRepository에서 delete 메소드 지원
    }
}
