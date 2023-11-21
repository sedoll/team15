package com.chunjae.edumarket.biz;

import com.chunjae.edumarket.entity.FileDTO;
import com.chunjae.edumarket.entity.FileVO;
import com.chunjae.edumarket.entity.Product;
import com.chunjae.edumarket.per.ProductMapper;
import com.chunjae.edumarket.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    // region 게시판 관련
    @Autowired
    private ProductMapper productMapper;
    
    // 모든 중고상품 목록 보기, 관리자
    @Override
    public List<Product> getAdmProductList(Page page) {
        return productMapper.getAdmProductList(page);
    }

    @Override
    public int getTotal(Page page) throws Exception {
        return productMapper.getTotal(page);
    }

    // 게시글 목록 보기
    @Override
    public List<Product> productList() {
        return productMapper.getProductList();
    }

    @Override
    public List<Product> productListWithPage(Page page) {
        return productMapper.productListWithPage(page);
    }

    // 게시글 목록 보기
    @Override
    public List<Product> productListRecent() {return productMapper.productListRecent();}

    @Override
    public List<Product> myProductList(Page page) {
        return productMapper.getMyProductList(page);
    }

    @Override
    public int getMyTotal(Page page) throws Exception {
        return productMapper.getMyTotal(page);
    }

    @Override
    public int getBuyerTotal(Page page) throws Exception {
        return productMapper.getBuyerTotal(page);
    }

    @Override // 내 구매 목록 추출
    public List<Product> productBuyerList(Page page) {
        return productMapper.productBuyerList(page);
    }

    // 글 상세 보기
    @Override
    public Product getProduct(Integer no) throws Exception {
        return productMapper.getProduct(no);
    }

    // 글 수정
    @Override
    public int productUpdate(Product product) throws Exception {
        return productMapper.productUpdate(product);
    }

    // 조회수 + 1
    @Override
    public void countUp(int no) throws Exception {
        productMapper.countUp(no);
    }

    @Override
    public int actUpdate(Product product) {
        return productMapper.actUpdate(product);
    }

    // region 파일 관련
    // 중고게시글 및 묶여있는 파일 삭제
    @Override
    public int removeFileboard(int postNo) throws Exception {
        int ck = productMapper.fileboardDelete(postNo);
        productMapper.removeFileAll(postNo);
        return ck;
    }

    @Override
    public void insertFileboard(FileVO fileboard) throws Exception {
        Product product = fileboard.getFileBoard();
        List<FileDTO> fileList = fileboard.getFileList();
        productMapper.fileBoardInsert(product);
        Product latestBoard = productMapper.latestFileboard();
        for(FileDTO file:fileList) {
            file.setPno(latestBoard.getNo());
            productMapper.fileInsert(file);
        }
    }

    @Override
    public List<FileDTO> getFileGroupList(int postNo) throws Exception {
        return productMapper.getFileGroupList(postNo);
    }

    @Override
    public void updateFileboard(FileVO fileboard) throws Exception {
        Product board = fileboard.getFileBoard();
        List<FileDTO> fileList = fileboard.getFileList();
        productMapper.fileboardUpdate(board);
        for(FileDTO file:fileList) {
            productMapper.fileUpdate(file);
        }
    }
    @Override
    public void removeFileAll(int postNo) throws Exception {
        productMapper.removeFileAll(postNo);
    }

    @Override
    public FileDTO thmbn(int no) throws Exception {
        return productMapper.thmbn(no);
    }

    // endregion
}
