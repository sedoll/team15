package com.chunjae.edumarket.ctrl;

import com.chunjae.edumarket.biz.ChatService;
import com.chunjae.edumarket.biz.UserService;
import com.chunjae.edumarket.entity.ChatRoom;
import com.chunjae.edumarket.entity.Euser;
import com.chunjae.edumarket.excep.NoSuchDataException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@CrossOrigin("http://localhost:8085")
@RequestMapping("/admin/*")
public class AdminController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private ChatService chatService;

    // 관리자 유저목록 확인페이지
    @GetMapping("/userList")
    public String getUserList(Model model){
        List<Euser> userList = userService.getUserList();
        if(userList.isEmpty()){
            throw new NoSuchDataException("No Such List");
        }
        model.addAttribute("msg", "회원목록을 로딩합니다.");
        model.addAttribute("userList", userList);
        return "admin/list";
    }
    
    // 관리자 메인페이지
    @GetMapping("/admIndex")
    public String getIndex(Model model) {
        return "admin/admIndex";
    }

    // 관리자 유저정보 확인페이지
    @GetMapping("/user")
    public String getUser(@RequestParam("name") String name, HttpSession session, Model model){
        Euser user = userService.getUser(name);
        if(user==null){
            throw new NoSuchDataException("No Such Data");
        }
        model.addAttribute("user", user);
        return "admin/get";
    }

    //회원등급변경(관리자)
    @GetMapping("/upgradeLevel")
    public String upgradeLevel(@RequestParam("name") String name, @RequestParam("lev") String lev, HttpSession session, Model model){
        int cnt = userService.updateLevel(name, lev);
        model.addAttribute("msg", "등급을 변경하였습니다.");
        return "redirect:/";
    }

    // 전체 채팅창 목록
    @GetMapping("chatList")
    public String chatList(@RequestParam("id") String id, Model model){
        List<ChatRoom> roomList = chatService.findAllRoom();
        logger.info(roomList.toString());
        model.addAttribute("roomList",roomList);
        return "admin/chatList";
    }
}
