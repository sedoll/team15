package com.chunjae.edumarket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {
    private Integer no;         // 상품 번호
    private String id;          // 작성자
    private String cate;        // 분류
    private String title;       // 제목
    private String content;     // 내용
    private Integer price;      // 가격
    private Integer cnt;        // 조회수
    private String resdate;     // 게시일
    private String act;         // 거래상태
    private String addr;        // 거래장소 주소
    private String buyer;       // 구매자
}
