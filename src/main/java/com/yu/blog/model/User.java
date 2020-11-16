package com.yu.blog.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity // User 클래스가 MySQL에 테이블 생성이 된다.
// @DynamicInsert : insert 시에 null인 필드를 제외시켜준다.
public class User {

    @Id // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
    private int id; // 시퀀스, auto_increment

    @Column(nullable = false, length = 100, unique=true)
    private String username; // 아이디

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false, length = 50)
    private String email;

    private String oauth; // oauth 로그인인지 파악

    // DB에는 RoleType이 없으므로 이게 String이라고 알려줌
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @CreationTimestamp // 시간이 자동으로 입력 됨
    private Timestamp createDate;
}
