package com.chunjae.edumarket.entity;

import lombok.Data;

@Data
public class Euser {
    private Integer id;
    private String name;
    private String password;
    private String username;
    private String email;
    private String addr1;
    private String addr2;
    private String postcode;
    private String tel;
    private String birth;
    private String regdate;
    private String lev;
    private String act;
}
