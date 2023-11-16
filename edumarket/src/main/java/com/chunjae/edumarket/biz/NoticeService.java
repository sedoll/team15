package com.chunjae.edumarket.biz;

import com.chunjae.edumarket.entity.Notice;

import java.util.List;

public interface NoticeService {
    public List<Notice> getNoticeList(); // 공지사항 목록
    public Notice getNotice(Integer no); // 상세 공지사항
    public int insertNotice(Notice notice); // 공지사항 추가
    public int updateNotice(Notice notice); // 공지사항 수정
    public int deleteNotice(Integer no); // 공지사항 삭제
    public int visitCount(Integer no); // 조회수
}
