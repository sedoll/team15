package com.chunjae.edumarket.ctrl;

import com.chunjae.edumarket.biz.BoardServiceImpl;
import com.chunjae.edumarket.entity.Board;
import com.chunjae.edumarket.entity.Comment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@CrossOrigin("http://localhost:8085")
@RequestMapping("/board/*")
public class BoardController {

    @Autowired
    private BoardServiceImpl boardService;

    // 게시글 입력 폼 이동
    // 일치하는 데이터가 없으면 null 반환
    @GetMapping("boardInsert")
    public String boardInsertForm(@RequestParam("name") String name, Model model) throws Exception {
        model.addAttribute("name", name);
        return "board/boardInsert";
    }

    // 게시글 입력
    // 일치하는 데이터가 없으면 null 반환
    @PostMapping ("boardInsert")
    public String boardInsert(Board board, Model model) throws Exception {
        Integer ck = boardService.insertBoard(board);
        if(ck == 1) {
            log.info("게시글 작성 성공");
            return "redirect:/common/boardList";
        } else {
            log.info("게시글 작성 실패");
            return "redirect:/";
        }
    }

    // 게시글 수정 폼 이동
    // 일치하는 데이터가 없으면 null 반환
    @GetMapping("boardUpdate")
    public String boardUpdateForm(@RequestParam("id") Integer id, Model model) throws Exception {
        Board board = boardService.getBoard(id);
        model.addAttribute("board", board);
        return "board/boardUpdate";
    }

    // 게시글 수정
    // 일치하는 데이터가 없으면 null 반환
    @PostMapping ("boardUpdate")
    public String boardUpdate(Board board, Model model) throws Exception {
        Integer ck = boardService.updatBoard(board);
        if(ck == 1) {
            log.info("게시글 수정 성공");
            return "redirect:/common/getBoard?id="+board.getId();
        } else {
            log.info("게시글 수정 실패");
            return "redirect:/";
        }
    }
    
    // 게시글 삭제
    @GetMapping("boardDelete")
    public String boardDelete(@RequestParam("id") Integer id, Model model) throws Exception {
        Integer ck = boardService.deleBoard(id);
        if(ck == 1) {
            log.info("게시글 삭제 성공");
            return "redirect:/common/boardList";
        } else {
            log.info("게시글 삭제 실패");
            return "redirect:/";
        }
    }

    // 댓글 작성
    @PostMapping("commentInsert")
    public String insertComment(Comment comment) throws Exception {
        log.info(comment.toString());
        Integer ck = boardService.inserBoardCom(comment);
        if(ck == 1) {
            log.info("댓글 작성 성공");
            return "redirect:/common/getBoard?id="+comment.getPar();
        } else {
            log.info("댓글 작성 실패");
            return "redirect:/";
        }
    }
    
    // 댓글 수정 폼 이동
    @GetMapping("commentUpdate")
    public String comUpdateGet(@RequestParam("id") Integer id, Model model) throws Exception {
        Comment com = boardService.getBoardCom(id);
        model.addAttribute("com", com);
        return "board/comUpdate";
    }

    // 댓글 수정
    @PostMapping("commentUpdate")
    public String comUpdatePost(@RequestParam("id") Integer id, @RequestParam("content") String content, Model model) throws Exception {
        Comment comment = boardService.getBoardCom(id);
        comment.setId(id);
        comment.setContent(content);
        int ck = boardService.upadetCom(comment);
        return "redirect:/common/getBoard?id="+comment.getPar();
    }

    // 댓글 삭제
    @GetMapping("commentDelete")
    public String comDelete(@RequestParam("id") Integer id, Model model) throws Exception {
        Comment comment = boardService.getBoardCom(id);
        int ck = boardService.deleCom(id);
        return "redirect:/common/getBoard?id="+comment.getPar();
    }
}
