package com.chunjae.edumarket.biz;

import com.chunjae.edumarket.entity.Board;
import com.chunjae.edumarket.entity.Comment;
import com.chunjae.edumarket.per.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService{

    @Autowired
    private BoardMapper boardMapper;

    // 게시글 목록 보기
    @Override
    public List<Board> boardList() {
        return boardMapper.getBoardList();
    }

    // 게시글 상세 보기
    @Override
    public Board getBoard(Integer id) {
        return boardMapper.getBoard(id);
    }

    // 게시글 작성
    @Override
    public Integer insertBoard(Board board) {
        return boardMapper.insertBoard(board);
    }
    
    // 게시글 수정
    @Override
    public int updatBoard(Board board) {
        return boardMapper.updatBoard(board);
    }
    
    // 게시글 삭제
    @Override
    public int deleBoard(Integer id) {
        return boardMapper.deleBoard(id);
    }
    
    // 댓글 목록
    @Override
    public List<Comment> CommentList(Integer par) {
        return boardMapper.commentList(par);
    }
    
    // 댓글 추가
    @Override
    public int inserBoardCom(Comment comment) {
        return boardMapper.inserBoardCom(comment);
    }

    @Override
    public Comment getBoardCom(Integer id) {
        return boardMapper.getBoardCom(id);
    }

    @Override
    public int upadetCom(Comment comment) {
        return boardMapper.upadetCom(comment);
    }

    @Override
    public int deleCom(Integer id) {
        return boardMapper.deleCom(id);
    }
}
