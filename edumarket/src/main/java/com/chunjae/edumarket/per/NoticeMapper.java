package com.chunjae.edumarket.per;

import com.chunjae.edumarket.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {
    List<Notice> getNoticeList(); // 공지사항 목록
    Notice getNotice(Integer no); // 상세 공지사항
    int insertNotice(Notice notice); // 공지사항 추가
    int updateNotice(Notice notice); // 공지사항 수정
    int deleteNotice(Integer no); // 공지사항 삭제
    int visitCount(Integer no); // 조회수
}
