package com.chunjae.edumarket.biz;

import com.chunjae.edumarket.entity.Board;
import com.chunjae.edumarket.entity.Comment;

import java.util.List;


public interface BoardService {

    // 게시글 목록 보기
    public List<Board> boardList();
    // 게시글 상세 보기
    public Board getBoard(Integer id);
    // 게시글 작성
    public Integer insertBoard(Board board);
    public int updatBoard(Board board); // 수정
    public int deleBoard(Integer id); // 삭제
    public List<Comment> CommentList(Integer par); // 댓글 리스트 목록
    public int inserBoardCom(Comment comment); // 댓글 입력
    public Comment getBoardCom(Integer id); // 댓글 정보 가져오기
    public int upadetCom(Comment comment); // 댓글 수정
    public int deleCom(Integer id); // 댓글 삭제
}
