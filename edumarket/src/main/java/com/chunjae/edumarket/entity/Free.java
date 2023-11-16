package com.chunjae.edumarket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Free {
    private Integer no;         // 번호
    private String id;        // 작성자
    private String title;       // 제목
    private String content;     // 내용
    private Integer visit;        // 조회수
    private String resdate;     // 게시일
}