package com.chunjae.edumarket.per;

import com.chunjae.edumarket.entity.*;
import com.chunjae.edumarket.utils.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    List<Product> getAdmProductList(Page page); // 모든 중고상품 리스트 목록
    int getTotal(Page page);        // 조건에 맞는 게시글 개수
    List<Product> getProductList(); // 리스트 목록
    List<Product> productListWithPage(Page page);
    List<Product> productListRecent(); // 최근 리스트 목록 8개
    List<Product> getMyProductList(Page page); // 내 상점
    int getMyTotal(Page page);
    int getBuyerTotal(Page page);
    List<Product> productBuyerList(Page page); // 내 구매
    Integer fileBoardInsert(Product fileboard) throws Exception; // 판매글 내용 db 저장
    Integer fileInsert(FileDTO file) throws Exception; // 판매글의 파일 db 저장
    int productUpdate(Product product) throws Exception; // 글 수정
    
    Product latestFileboard() throws Exception;
    List<FileDTO> getFileGroupList(int postNo) throws Exception;
    Product getProduct(Integer postNo) throws Exception;

    int fileboardDelete(int no) throws Exception; // product 제거
    void fileboardUpdate(Product product) throws Exception; // 거래글 수정
    void fileUpdate(FileDTO fileDTO) throws Exception; // 파일 수정
    void removeFileAll(int postNo) throws Exception; // 해당 상품의 모든 파일 제거
    void countUp(int no) throws Exception; // 게시글 조회수 +1
    FileDTO thmbn(int no) throws Exception; // 썸네일 이미지
    int actUpdate(Product product); // 상품 거래 완료 처리
}