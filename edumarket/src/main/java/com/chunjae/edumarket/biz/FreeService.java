package com.chunjae.edumarket.biz;

import com.chunjae.edumarket.entity.Free;
import com.chunjae.edumarket.entity.FreeComment;

import java.util.List;

public interface FreeService {
    public List<Free> getFreeList(); // 리스트 목록
    public Free getFree(Integer no); // 상세
    public int insertFree(Free free); // 추가
    public int updateFree(Free free); // 수정
    public int deleteFree(Integer no); // 삭제
    public int visitCount(Integer no); // 조회수

    // 댓글

    public List<FreeComment> commentList(Integer par);
    public int insertFreeComment(FreeComment freeComment);
    public FreeComment getFreeComment(Integer no);
    public int updateFreeComment(FreeComment freeComment);
    public int deleteFreeComment(Integer no);
}
