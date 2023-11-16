package com.chunjae.edumarket.per;

import com.chunjae.edumarket.entity.Free;
import com.chunjae.edumarket.entity.FreeComment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FreeMapper {
    List<Free> getFreeList(); // 리스트 목록
    Free getFree(Integer no); // 상세
    int insertFree(Free free); // 추가
    int updateFree(Free free); // 수정
    int deleteFree(Integer no); // 삭제
    int visitCount(Integer no); // 조회수

    // 댓글

    List<FreeComment> commentList(Integer par);
    int insertFreeComment(FreeComment freeComment);
    FreeComment getFreeComment(Integer no);
    int updateFreeComment(FreeComment freeComment);
    int deleteFreeComment(Integer no);
    
}
