package com.example.bbs;

import com.example.bbs.entity.Board;
import com.example.bbs.entity.Comment;
import com.example.bbs.repository.BoardRepository;
import com.example.bbs.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@SpringBootTest
class BbsApplicationTests {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    void testComment() {

            Optional<Board> boardComment = this.boardRepository.findById(1);
            assertTrue(boardComment.isPresent());
            Board b = boardComment.get();
            Comment c = new Comment();
            c.setContent("Comment Test");
            c.setBoard(b); //어떤 질문에 달린 댓글인지 알기 위해 board 객체 필요
            c.setCreateDate(LocalDateTime.now());
            this.commentRepository.save(c);

    }


    @Test
    void testPost() {
        for (int i = 0; i < 101; i++) {
            Board b = new Board();
            b.setTitle("post test title "+i);
            b.setContent("post test content "+i);
            b.setCreatedAt(LocalDateTime.now());
            this.boardRepository.save(b);
        }

    }


    @Test
    void contextLoads() {
    }

}
