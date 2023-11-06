package com.example.bbs.controller;

import com.example.bbs.dto.CommentForm;
import com.example.bbs.entity.Board;
import com.example.bbs.entity.Comment;
import com.example.bbs.entity.SiteUser;
import com.example.bbs.service.BoardService;
import com.example.bbs.service.CommentService;
import com.example.bbs.service.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/comment")
@Controller
public class CommentController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    //@RequestParam String content 항목
    //템플릿에서 답변으로 입력한 내용(content)을 얻기 위해 추가
    //템플릿의 답변 내용에 해당하는 textarea의 name 속성명이 content이기 때문에 여기서도 변수명을 content로 사용
    public String createComment(Model model, @PathVariable("id") Integer id, @Valid CommentForm commentForm, BindingResult bindingResult, Principal principal) {
        Board board = this.boardService.getBoard(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        //save comment
        if(bindingResult.hasErrors()) {
            //boardDetail 템플릿은 board 객체가 필요하기 때문에 model 객체에 board를 저장한 후 boardDetail 렌더링
            model.addAttribute("board", board);
            return "boardDetail";
        }
        this.commentService.create(board, commentForm.getContent(), siteUser);
        return String.format("redirect:/board/detail/%s",id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String commentModify (CommentForm commentForm, @PathVariable("id") Integer id, Principal principal) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "YOU DON'T HAVE PERMISSION TO MODIFY");
        }
        commentForm.setContent(comment.getContent());
        return "commentForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String commentModify (@Valid CommentForm commentForm, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "commentForm";
        }
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "YOU DON'T HAVE PERMISSION TO MODIFY");
        }
        this.commentService.modify(comment, commentForm.getContent());
        return String.format("redirect:/board/detail/%s", comment.getBoard().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String commentDelete(Principal principal, @PathVariable("id") Integer id) {
        Comment comment = this.commentService.getComment(id);
        if(!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "YOU DON'T HAVE PERMISSION TO DELETE");
        }
        this.commentService.delete(comment);
        return String.format("redirect:/board/detail/%s", comment.getBoard().getId());
    }
}
