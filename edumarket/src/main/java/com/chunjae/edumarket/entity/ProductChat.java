package com.chunjae.edumarket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductChat {
    private Integer no;
    private String title;
    private String roomId;
    private String buyer;
    private String seller;
    private String name;
    private String pact;
    private String cact;
}
