package com.chunjae.edumarket.ctrl;

import com.chunjae.edumarket.biz.ChatService;
import com.chunjae.edumarket.entity.ChatMessage;
import com.chunjae.edumarket.entity.ChatRoom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/chat/*")
public class ChatController {

    @Autowired
    private ChatService chatService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    // 채팅방 목록
    @GetMapping("chatList")
    public String chatList(Model model){
        List<ChatRoom> roomList = chatService.findAllRoom();
        logger.info(roomList.toString());
        model.addAttribute("roomList",roomList);
        return "chat/chatList";
    }
    
    // 채팅방 만들기
    @PostMapping("createRoom")  //방을 만들었으면 해당 방으로 가야지.
    public String createRoom(Model model, ChatRoom chatRoom, HttpServletResponse res, String username) throws Exception {
        int ck = chatService.findChatDist(chatRoom); // 채팅방이 존재하는지 안하는지 검사
        if(ck == 0) {
            int ck2 = chatService.createRoom(chatRoom);
            return "redirect:/chat/chatList";  //만든사람이 채팅방 1빠로 들어가게 됩니다
        } else {
            res.setContentType("text/html;charset=UTF-8");
            PrintWriter out = res.getWriter();
            out.println("<script>alert('해당 판매자와의 채팅방이 존재합니다.'); window.location.href='/common/getProduct?no=" + chatRoom.getPno() + "';</script>");
            out.flush();
            return null;  // You can also return an empty string or another view name if needed
//            return "redirect:/common/getProduct?no="+chatRoom.getPno();
        }
    }
    
    // 채팅방 들어가기
    @GetMapping("chatRoom")
    public String chatRoom(Model model, @RequestParam String roomId) throws Exception{
        List<ChatMessage> chatMsg = chatService.findChatById(roomId);
        model.addAttribute("chat", chatMsg);
        model.addAttribute("roomId",roomId);   //현재 방에 들어오기위해서 필요한데...... 접속자 수 등등은 실시간으로 보여줘야 돼서 여기서는 못함
        return "chat/chatRoom";
    }
    
    // 채팅내역 db에 저장
    @PostMapping("insertChat")
    public void insertChat(ChatMessage chatMsg) throws Exception{
        chatService.insertChat(chatMsg);
    }
}