package com.example.bbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @GetMapping("/bbs")
    @ResponseBody // @ResponseBody 애너테이션은 URL 요청에 대한 응답으로 문자열을 리턴하라는 의미
    //@ResponseBody 애너테이션을 생략한다면 "index"라는 이름의 템플릿 파일을 찾게 됨
    public String index() {
        return "index";
    }

    //root url
    @GetMapping("/")
    public String root() {
        return "redirect:/board/list";
    }

}
