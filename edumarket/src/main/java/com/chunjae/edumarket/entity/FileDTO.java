package com.chunjae.edumarket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileDTO {
    private Integer no;
    private Integer pno;
    private String savefolder;
    private String originfile;
    private String savefile;
    private Long filesize;
    private String uploaddate;
}
