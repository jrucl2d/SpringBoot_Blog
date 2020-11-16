package com.yu.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob // 대용량 데이터
    private String content; // 섬머노트 라이브러리 사용. html 태그가 섞여서 디자인 됨

    private int count; // 조회수

    @ManyToOne(fetch = FetchType.EAGER) // User은 Board 내부에 하나밖에 없을테니 기본 전략이 eager이다.
    @JoinColumn(name="userId") // 실제로는 userId 컬럼으로 들어감
    private User user; // DB는 오브젝트 저장 불가(FK). 자바는 오브젝트 저장 가능

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) // Cascade 옵션 : 보드 삭제하면 댓글도
    @JsonIgnoreProperties({"board"}) // 무한 참조를 방지. 다음 Reply 안에서의 board는 가져오지 마라. 이미 Board에서 Reply를 가져온 것.
    @OrderBy("id desc") // id 값으로 내림차순 정렬
    private List<Reply> reply; // Reply 엔티티 내부의 필드 이름 'board'를 통해 join할 때 사용

    @CreationTimestamp
    private Timestamp creatDate;
}
