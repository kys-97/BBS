package com.example.bbs.controller;

import com.example.bbs.dto.BoardForm;
import com.example.bbs.dto.CommentForm;
import com.example.bbs.entity.Board;
import com.example.bbs.entity.SiteUser;
import com.example.bbs.repository.BoardRepository;
import com.example.bbs.service.BoardService;
import com.example.bbs.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.awt.font.TextHitInfo;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor // @RequiredArgsConstructor 애너테이션으로 questionRepository 속성을 포함하는 생성자를 생성
//final이 붙은 속성을 포함하는 생성자를 자동으로 생성하는 역할
//스프링 의존성 주입 규칙에 의해 BoardRepository 객체가 자동으로 주입
@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

    @GetMapping("/list")
    //@ResponseBody
    //Model 객체는 자바 클래스와 템플릿 간의 연결고리 역할을 한다. Model 객체에 값을 담아두면 템플릿에서 그 값을 사용
    //URL에 페이지 파라미터 page가 전달되지 않은 경우 디폴트 값으로 0
    public String boardList(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Board> paging = this.boardService.getList(page, kw);
       //템플릿에 Page<Question> 객체인 paging을 전달
        model.addAttribute("paging", paging);
        model.addAttribute("kw",kw);
        return "boardList";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, CommentForm commentForm) {
       Board board = this.boardService.getBoard(id);
       model.addAttribute("board",board);
        return "boardDetail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    //BoardForm boardForm
    //boardForm.html -> write 버튼을 통해 get방식으로 요청 -> 이 경우에도 th:object에 의해 BoardForm 객체 필요
    public String boardCreate(BoardForm boardForm) {
        return "boardForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    //indingResult 매개변수는 항상 @Valid 매개변수 바로 뒤에 위치
    public String boardCreate(@Valid BoardForm boardForm, BindingResult bindingResult, Principal principal) {
        if(bindingResult.hasErrors()) {
            return "boardForm";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.boardService.create(boardForm.getTitle(), boardForm.getContent(), siteUser);
        return "redirect:/board/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String boardModify(BoardForm boardForm, @PathVariable("id") Integer id, Principal principal) {
        Board board = this.boardService.getBoard(id);
        if(!board.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "YOU DON'T HAVE PERMISSION TO MODIFY.");
        }
        boardForm.setTitle(board.getTitle());
        boardForm.setContent(board.getContent());
        return "boardForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid BoardForm boardForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "boardForm";
        }
        Board board = this.boardService.getBoard(id);
        if (!board.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "YOU DON'T HAVE PERMISSION TO MODIFY.");
        }
        this.boardService.modify(board, boardForm.getTitle(), boardForm.getContent());
        return String.format("redirect:/board/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String boardDelete(Principal principal, @PathVariable("id") Integer id) {
        Board board = this.boardService.getBoard(id);
        if(!board.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "YOU DON'T HAVE PERMISSION TO DELETE");
        }
        this.boardService.delete(board);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/like/{id}")
    public String boardLike(Principal principal, @PathVariable("id") Integer id) {
        Board board = this.boardService.getBoard(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.boardService.like(board,siteUser);
        return  String.format("redirect:/board/detail/%s", id);
    }
}
