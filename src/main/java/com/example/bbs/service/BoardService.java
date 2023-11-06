package com.example.bbs.service;

import com.example.bbs.DataNotFoundException;
import com.example.bbs.entity.Board;
import com.example.bbs.entity.Comment;
import com.example.bbs.entity.SiteUser;
import com.example.bbs.repository.BoardRepository;
import jakarta.persistence.criteria.*;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

//    public List<Board> getList() {
//        return this.boardRepository.findAll();
//    }

    //paging
    public Page<Board> getList(int page, String kw) {
        //작성일시 역순 조회
        //게시판 -> 최근 게시물이 가장 먼저
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Board> spec = search(kw);
        return this.boardRepository.findAllByKeyword(kw, pageable);
    }
    public Board getBoard(Integer id) {
        Optional<Board> board = this.boardRepository.findById(id);
        if(board.isPresent()) {
            return board.get();
        }
        else {
            throw new DataNotFoundException("Board not found!");
        }
    }

    public void create(String title, String content, SiteUser user) {
        Board b = new Board();
        b.setTitle(title);
        b.setContent(content);
        b.setCreatedAt(LocalDateTime.now());
        b.setAuthor(user);
        this.boardRepository.save(b);
    }

    public void modify(Board board, String title, String content) {
        board.setTitle(title);
        board.setContent(content);
        board.setModifyDate(LocalDateTime.now());
        this.boardRepository.save(board);
    }

    public void delete (Board board) {
        this.boardRepository.delete(board);
    }

    public void like(Board board, SiteUser siteUser) {
        board.getLike().add(siteUser);
        this.boardRepository.save(board);
    }
    //search
    //search 메서드는 검색어(kw)를 입력받아 쿼리의 조인문과 where문을 생성하여 리턴하는 메서드
    private Specification<Board> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            //Root<Board> b: 기준을 의미. Board 엔티티의 객체
            public Predicate toPredicate(Root<Board> b, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Board, SiteUser> u1 = b.join("author", JoinType.LEFT);
                Join<Board, Comment> c = b.join("commentList", JoinType.LEFT);
                Join<Comment, SiteUser> u2 = c.join("author", JoinType.LEFT);
                return cb.or(cb.like(b.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(b.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(c.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }



}
