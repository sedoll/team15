package com.chunjae.edumarket.biz;

import com.chunjae.edumarket.entity.Free;
import com.chunjae.edumarket.entity.FreeComment;
import com.chunjae.edumarket.per.FreeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FreeServiceImpl implements FreeService {

    @Autowired
    private FreeMapper freeMapper;

    @Override
    public List<Free> getFreeList() {
        return freeMapper.getFreeList();
    }

    @Override
    public Free getFree(Integer no) {
        return freeMapper.getFree(no);
    }

    @Override
    public int insertFree(Free free) {
        return freeMapper.insertFree(free);
    }

    @Override
    public int updateFree(Free free) {
        return freeMapper.updateFree(free);
    }

    @Override
    public int deleteFree(Integer no) {
        return freeMapper.deleteFree(no);
    }

    @Override
    public int visitCount(Integer no) {
        return freeMapper.visitCount(no);
    }
    // 댓글

    @Override
    public List<FreeComment> commentList(Integer par) {
        return freeMapper.commentList(par);
    }

    @Override
    public int insertFreeComment(FreeComment freeComment) {
        return freeMapper.insertFreeComment(freeComment);
    }


    @Override
    public FreeComment getFreeComment(Integer no) {
        return freeMapper.getFreeComment(no);
    }

    @Override
    public int updateFreeComment(FreeComment freeComment) {
        return freeMapper.updateFreeComment(freeComment);
    }

    @Override
    public int deleteFreeComment(Integer no) {
        return freeMapper.deleteFreeComment(no);
    }
}
