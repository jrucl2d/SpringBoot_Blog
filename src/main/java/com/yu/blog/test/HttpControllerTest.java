package com.yu.blog.test;

import org.springframework.web.bind.annotation.*;

// 사용자가 요청 -> 응답(HTML 파일) : @Controller를 사용함
// 사용자가 요청 -> 응답(Data 파일) : @RestController를 사용함
@RestController
public class HttpControllerTest {

    @GetMapping("/http/lombok")
    public String lombokText(){
        Member m =new Member(1, "yu", "1234", "email");
        // 빌더 패턴 사용시 id를 직접 만들어주고, 파라미터의 순서를 지키지 않아도 됨
        // Member m = Member.builder().username("yu").password("1234").email("a@a.com").build()
        System.out.println(m.getId());
        m.setId(123);
        System.out.println(m.getId());
        return "lombok test";
    }

    @GetMapping("/http/get") // 인터넷 브라우저 요청은 get 요청만 가능
    public String getTest(@RequestParam int id, @RequestParam String username){
        // @RequestParam으로 queryString을 받을 수 있음
        return "get 요청 : " + id + " " + username;
    }
    @GetMapping("/http/get2")
    public String getTest2(Member member){
        // http://localhost:8080/http/get2?id=3&username=%EB%B0%94%EB%B3%B4&password=1234&email=aa@a.com
        // 쿼리 스트링에서 Member 변수로 매핑해주는 것이 MessageConverter가 하는 일(스프링 부트)
        // 클래스를 넘겨줘도 받을 수 있다. 근데 만약 변수 명이 틀리거나(emaal) 없으면 null이 반환됨
        return "get 요청 : " + member.getId() + " " + member.getUsername() + " " + member.getPassword()
                + member.getEmail();
    }
    @PostMapping("/http/post")
    public String postTest(@RequestBody Member member){ // @RequestBody String text 이런 식으로 받거나 클래스로 받기
        // Json 파싱을 MessageConverter가 해줌
        // html form 태그 안에 넣어서 보내는 것 : x-www-form-urlencded 방식
        return "post 요청 : " + member.getId() + " " + member.getUsername() + " " + member.getPassword()
                + member.getEmail();
    }
    @PutMapping("/http/put")
    public String putTest(){
        return "put 요청";
    }
    @DeleteMapping("/http/delete")
    public String deleteTest(){
        return "delete 요청";
    }
}
