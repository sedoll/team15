package com.chunjae.edumarket.per;

import com.chunjae.edumarket.entity.Board;
import com.chunjae.edumarket.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<Board> getBoardList(); // 리스트 목록
    Board getBoard(Integer no); // 상세
    int insertBoard(Board Board); // 추가
    int updatBoard(Board Board); // 수정
    int deleBoard(Integer no); // 삭제
    List<Comment> commentList(Integer par); // 댓글 리스트 목록
    int inserBoardCom(Comment comment); // 댓글 입력
    Comment getBoardCom(Integer no); // 댓글 정보 추출
    int upadetCom(Comment comment); // 댓글 수정
    int deleCom(Integer no); // 댓글 삭제
}