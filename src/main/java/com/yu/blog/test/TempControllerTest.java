package com.yu.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempControllerTest {

    // http://localhost:8000/blog/temp/home <- context path가 blog로 application.yml에 설정됨
    @GetMapping("/temp/home")
    public String tempHome(){
        // @Controller : 해당 경로에 있는 파일을 보내줌
        // 파일 리턴 기본 경로 : src/main/resources/static
        // 리턴명 : /home.html
        // 풀경로 : src/main/resources/static/home.html
        return "/home.html";
    }

    @GetMapping("/temp/jsp")
    public String tempJsp(){
        // prefix : /WEB-INF/views/
        // sufix : .jsp
        return "test";
    }
}
