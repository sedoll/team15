package com.chunjae.edumarket.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page {
    private final static int pageCount = 5;
    private int blockStartNum = 0;
    private int blockLastNum = 0;
    private int lastPageNum = 0;
    private int curPageNum = 1;
    private int postCount = 10;
    private int postStart = 0;
    private int pageBlockNum = 1;
    private int totalBlockNum = 1;
    private int totalPageCount = 1;
    // 검색어 입력 시 들어오는 데이터
    private String type;
    private String keyword;
    private String category;
    // 중고거래에 필요한 데이터
    private String location;
    // 사용자 정보에 필요한 데이터
    private String name;

    public Page(int curPageNum) {
        this.curPageNum = curPageNum;
    }

    public Page(int curPageNum, String category) {
        this.curPageNum = curPageNum;
        this.category = category;
    }

    public Page(int curPageNum, String type, String keyword) {
        this.curPageNum = curPageNum;
        this.type = type;
        this.keyword = keyword;
    }

    public Page(int curPageNum, String type, String keyword, String category) {
        this.curPageNum = curPageNum;
        this.type = type;
        this.keyword = keyword;
        this.category = category;
    }

    public void makePage(int total) {
        // 전체 페이지 개수 구하기
        this.postStart = (this.curPageNum - 1) * this.postCount;
        this.pageBlockNum = (int)Math.floor(this.curPageNum / pageCount);

        int comp = pageCount * postCount;
        if( total % comp == 0 ) {
            this.totalBlockNum = (int)Math.floor(total/comp);
        } else {
            this.totalBlockNum = (int)Math.floor(total/comp) + 1;
        }
        if(total % postCount == 0){
            totalPageCount = (int)Math.floor(total/postCount);
        } else {
            totalPageCount = (int)Math.floor(total/postCount)+1;
        }

        // 현재 페이지가 속한 block의 시작 번호, 끝 번호 계산
        int blockNum = 0;

        blockNum = (int)Math.floor((this.curPageNum-1)/ pageCount);
        blockStartNum = (pageCount * blockNum) + 1;

        comp = 0;
        if(total % postCount == 0){
            comp = (int)Math.floor(total/ postCount);
        } else {
            comp = (int)Math.floor(total/ postCount)+1;
        }
        blockLastNum = blockStartNum + (pageCount-1);
        if(blockLastNum >= comp){
            blockLastNum = comp;
        }

        // 총 페이지의 마지막 번호 구하기
        if(total % pageCount == 0) {
            lastPageNum = (int) Math.floor(total/pageCount);
        } else {
            lastPageNum = (int) Math.floor(total/pageCount) + 1;
        }
    }
}
