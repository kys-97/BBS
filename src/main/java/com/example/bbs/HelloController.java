package com.example.bbs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * Controller annotation
 * HelloController class -> controller 기능을 수행한다는 의미
 * 이 어노테이션이 있어야 스프링부트 프레임워크가 클래스를 컨트롤러로 인식함
 * */
@Controller
public class HelloController {

    /*
    * GetMapping annotation
    * url 요청이 발생하면 hello method 실행
    * /hello url, hello method mapping
    *
    * ResponseBody annotation
    * hello method의 응답 결과가 문자열임을 나타냄
    * hello world라는 문자열을 리턴하기 때문에 화면에는 hello world 문자열이 나갈 것
    * */
    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Spring Boot BBS Project!";
    }


}
