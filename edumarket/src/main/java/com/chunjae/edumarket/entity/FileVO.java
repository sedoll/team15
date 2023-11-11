package com.chunjae.edumarket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileVO {
    private Product fileBoard;
    private List<FileDTO> fileList;
}
