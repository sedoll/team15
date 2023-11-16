package com.chunjae.edumarket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Notice {
    private Integer no; // 번호
    private String title; // 제목
    private String content; // 내용
    private String resdate; // 작성일
    private Integer visit; // 조회수
}
