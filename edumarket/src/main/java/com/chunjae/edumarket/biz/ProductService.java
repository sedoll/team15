package com.chunjae.edumarket.biz;

import com.chunjae.edumarket.entity.FileDTO;
import com.chunjae.edumarket.entity.FileVO;
import com.chunjae.edumarket.entity.Product;

import java.util.List;


public interface ProductService {

    // 게시글 목록 보기
    public List<Product> productList();

    // 게시글 상세 보기
    public Product getProduct(Integer no) throws Exception;

    public int updatProduct(Product product); // 수정

    public void insertFileboard(FileVO fileboard) throws Exception;
    public List<FileVO> getFileList() throws Exception;
    public List<FileDTO> getFileGroupList(int postNo) throws Exception;
    public FileVO getFileObject(int no) throws Exception;
    public int removeFileboard(int postNo) throws Exception; // product, file 삭제
    public void fileRemove(int no) throws Exception;
    public FileDTO getFile(int no) throws Exception;
    public void updateFileboard(FileVO fileboard) throws Exception;
    public void removeFileAll(int postNo) throws Exception;
}
