package com.chunjae.edumarket.ctrl;

import com.chunjae.edumarket.biz.ChatService;
import com.chunjae.edumarket.biz.ProductService;
import com.chunjae.edumarket.biz.UserService;
import com.chunjae.edumarket.entity.ChatRoom;
import com.chunjae.edumarket.entity.Euser;
import com.chunjae.edumarket.entity.FileDTO;
import com.chunjae.edumarket.entity.Product;
import com.chunjae.edumarket.excep.NoSuchDataException;
import com.chunjae.edumarket.utils.Page;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@CrossOrigin("http://localhost:8085")
@RequestMapping("/user/*")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ChatService chatService;

    // 마이페이지
    @GetMapping("mypage")
    public String mypage(Principal principal, Model model) throws Exception {
        // 사용자 정보
        Euser user = userService.getUser(principal.getName());
        if(user==null){
            throw new NoSuchDataException("No Such Data");
        }
        model.addAttribute("user", user);

        // 채팅 목록
        List<ChatRoom> roomList = chatService.findAllRoom(principal.getName());
        logger.info(roomList.toString());
        model.addAttribute("roomList",roomList);

        // 사용자 판매 목록
        Page page = new Page();
        page.setPostCount(6);
        page.makePage(6);
        page.setName(principal.getName());
        List<Product> productList = productService.myProductList(page);
        List<FileDTO> fileList = new ArrayList<>();
        for (Product pro:productList) {
            FileDTO dto = productService.thmbn(pro.getNo());
            fileList.add(dto);
        }
        model.addAttribute("productList", productList);
        model.addAttribute("fileList", fileList);

        // 내가 구매한 상품 목록
        List<Product> paymentList = productService.productBuyerList(page);
        List<FileDTO> payFileList = new ArrayList<>();
        for (Product pro:paymentList) {
            FileDTO dto = productService.thmbn(pro.getNo());
            payFileList.add(dto);
        }
        model.addAttribute("paymentList", paymentList);
        model.addAttribute("payFileList", payFileList);

        return "user/mypage";
    }
    
    // 유저 정보 페이지
    @PostMapping("userIndex")
    public String getUserInfo(@RequestParam("name") String name, Model model){
        Euser user = userService.getUser(name);
        if(user==null){
            throw new NoSuchDataException("No Such Data");
        }
        model.addAttribute("user", user);
        return "user/userIndex";
    }

    //탈퇴
    @GetMapping("withdraw")
    public String userDel(@RequestParam("id") Integer id, Model model){
        int cnt = userService.getWithdraw(id);
        return "redirect:/";
    }

    //계정 삭제
    @GetMapping("removeUser/{name}")
    public String removeUser(@PathVariable("name") String name, Model model){
        int cnt = userService.removeUser(name);
        if(cnt == 0){
            throw new NoSuchDataException("No Delete Process Data");
        }
        return "redirect:/";
    }

    // 회원정보수정
    @PostMapping("updateUserPro")
    public String updateUserPro(Euser user, Model model){
        Euser euser = userService.getUserByName(user.getName());
        int cnt = 0;
        log.info(euser.toString());
        if(user.getPassword().equals(euser.getPassword())){
            cnt = userService.updatePasswordNoChange(user);
        } else {
            cnt = userService.updateUser(user);
        }
        if(cnt == 0){
            throw new NoSuchDataException("No Update Process Data");
        }
        model.addAttribute("msg","회원정보를 수정하였습니다.");
        return "redirect:mypage";
    }

}
