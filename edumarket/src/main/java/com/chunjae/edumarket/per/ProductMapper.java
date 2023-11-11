package com.chunjae.edumarket.per;

import com.chunjae.edumarket.entity.FileDTO;
import com.chunjae.edumarket.entity.FileVO;
import com.chunjae.edumarket.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    List<Product> getProductList(); // 리스트 목록
    int updatProduct(Product product); // 수정
    int deleProduct(Integer id); // 삭제
    public Integer fileBoardInsert(Product fileboard) throws Exception; // 판매글 내용 db 저장
    Integer fileInsert(FileDTO file) throws Exception; // 판매글의 파일 db 저장

    public Product latestFileboard() throws Exception;
    public List<FileVO> getFileList() throws Exception; // 리스트 목록
    public List<FileDTO> getFileGroupList(int postNo) throws Exception;
    public Product getProduct(Integer postNo) throws Exception;
    public FileVO getFileObject(int no) throws Exception;

    int fileboardDelete(int no) throws Exception; // product 제거
    int fileDelete(int no) throws Exception; // product 에 묶여있는 파일 제거
    public void fileRemove(int no) throws Exception;
    public FileDTO getFile(int no) throws Exception;
    public void updateFileboard(FileVO fileboard) throws Exception;
    public void removeFileAll(int postNo) throws Exception;
}