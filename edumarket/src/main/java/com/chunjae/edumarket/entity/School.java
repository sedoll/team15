package com.chunjae.edumarket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class School {
    private String eo_code;
    private String eo_name;
    private String sc_code;
    private String sc_name;
}
