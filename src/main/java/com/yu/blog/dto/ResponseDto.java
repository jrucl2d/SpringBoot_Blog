package com.yu.blog.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto<T> {
    int status; // 응답의 상태
    T data; // 응답의 데이터
}
