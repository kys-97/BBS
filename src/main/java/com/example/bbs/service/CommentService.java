package com.example.bbs.service;

import com.example.bbs.DataNotFoundException;
import com.example.bbs.entity.Board;
import com.example.bbs.entity.Comment;
import com.example.bbs.entity.SiteUser;
import com.example.bbs.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public void create(Board board, String content, SiteUser author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setBoard(board);
        comment.setAuthor(author);
        this.commentRepository.save(comment);
        //입력으로 받은 board, content를 사용해 comment객체 생성하고 저장
    }

    public Comment getComment(Integer id) {
        Optional<Comment> comment = this.commentRepository.findById(id);
        if(comment.isPresent()) {
            return comment.get();
        }
        else {
            throw new DataNotFoundException("COMMENT NOT FOUND");
        }
    }

    public void modify(Comment comment, String content) {
        comment.setContent(content);
        comment.setModifyDate(LocalDateTime.now());
        this.commentRepository.save(comment);
    }

    public void delete(Comment comment) {
        this.commentRepository.delete(comment);
    }
}
