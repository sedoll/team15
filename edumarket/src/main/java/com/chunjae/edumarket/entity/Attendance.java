package com.chunjae.edumarket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
    private int ano;
    private String id;
    private String attend;
    private String givaway;

    public Attendance(String id, String givaway) {
        this.id = id;
        this.givaway = givaway;
    }
}
