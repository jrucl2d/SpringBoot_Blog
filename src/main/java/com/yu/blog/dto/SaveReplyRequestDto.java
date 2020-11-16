package com.yu.blog.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SaveReplyRequestDto {
    private int userId;
    private int boardId;
    private String content;
}
