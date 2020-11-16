package com.yu.blog.test;
import lombok.*;
// @Data // Getter, Setter, @ToString 등
@Getter
@Setter
@ToString
@AllArgsConstructor // 생성자, Member m = Member(id, username, password, email)
@NoArgsConstructor // 빈 생성자, Member m = Member();
// @RequiredArgsConstructor // final 붙은 변수에 대해서 생성자를 만들어줌
public class Member {
    private int id;
    private String username;
    private String password;
    private String email;
    
    // @Builder // 빌더 패턴 사용
//    public Member(int id, String username, String password, String email) {
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        this.email = email;
//    }
}
