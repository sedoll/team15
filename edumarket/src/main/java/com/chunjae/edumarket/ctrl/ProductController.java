package com.chunjae.edumarket.ctrl;

import com.chunjae.edumarket.biz.ChatService;
import com.chunjae.edumarket.biz.ProductServiceImpl;
import com.chunjae.edumarket.entity.*;
import com.chunjae.edumarket.utils.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Slf4j
@Controller
@CrossOrigin("http://localhost:8085")
@RequestMapping("/product/*")
public class ProductController {

    // 실제 업로드 디렉토리
    // thymeleaf 에서는 외부에 지정하여 사용해야 한다.
    // jsp와는 다르게 webapp이 없기 때문이다.
    // resources는 정적이라 업데이트 되어도 파일을 못 찾기에 서버를 재 시작 해야함
    @Value("${spring.servlet.multipart.location}")
    String uploadFolder;

    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private ChatService chatService;


    // 게시글 입력 폼 이동
    // 일치하는 데이터가 없으면 null 반환
    @GetMapping("productInsert")
    public String productInsertForm(@RequestParam("name") String name, Model model) throws Exception {
        model.addAttribute("name", name);
        return "product/productInsert";
    }

    // 중고거래 추가 폼이동
    @GetMapping("fileUpload")
    public String fileUploadForm() {
        return "product/productInsert";
    }

    // 중고거래 추가
    @PostMapping("fileUpload")
    public String fileUpload(@RequestParam("files") List<MultipartFile> files,
                             @RequestParam Map<String, String> params,
                             HttpServletRequest req,
                             Model model) throws Exception {

        // Create the 'board' object
        Product board = new Product();
        board.setId(params.get("id"));
        board.setTitle(params.get("title"));
        board.setContent(params.get("content"));
        board.setAddr(params.get("addr"));
        board.setCate(params.get("cate"));
        board.setPrice(Integer.valueOf(params.get("price")));

        File folder = new File(uploadFolder);
        if (!folder.exists())
            folder.mkdirs();
        log.info("-----------------------------------");
        log.info(" 현재 프로젝트 홈 : " + req.getContextPath());
        log.info(" 지정한 경로 : " + uploadFolder);
        log.info(" 요청 URL : " + req.getServletPath());
        log.info(" 프로젝트 저장 경로 : " + uploadFolder);

        //여러 파일 반복 저장
        List<FileDTO> fileList = new ArrayList<>();
        // 파일 리스트를 순회하며 각 파일 처리
        for (MultipartFile file : files) {
            if (!file.getOriginalFilename().isEmpty()) {
                // 파일 처리 로직 시작
                String randomUUID = UUID.randomUUID().toString();  // 파일 이름 중복 방지를 위한 랜덤 UUID 생성
                String OriginalFilename = file.getOriginalFilename();  // 실제 파일 이름
                String Extension = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));  // 파일 확장자 추출
                String saveFileName = randomUUID + Extension;  // 저장할 파일 이름 생성

                // ... (기존 파일 처리 로직)
                FileDTO data = new FileDTO();
                data.setSavefolder(uploadFolder);
                data.setOriginfile(file.getOriginalFilename());
                data.setSavefile(saveFileName);
                data.setFilesize(file.getSize());
                Date today = new Date();
                data.setUploaddate(today.toString());
                fileList.add(data);

                // 파일 저장
                File saveFile = new File(uploadFolder, saveFileName);
                try {
                    file.transferTo(saveFile);
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                    // 예외 처리
                }
            }
        }

        FileVO fileboard = new FileVO();
        fileboard.setFileList(fileList);
        fileboard.setFileBoard(board);
        productService.insertFileboard(fileboard);
        return "redirect:/common/productList";
    }

    // 중고상품게시글 및 묶여있던 파일 통합 삭제
    @GetMapping("productDelete")
    public String productDelete(@RequestParam("no") Integer postNo, HttpServletRequest req) throws Exception {

        //실제 파일 삭제 로직
        //파일 경로 지정

        List<FileDTO> fileList = productService.getFileGroupList(postNo);
        for (FileDTO fileobj : fileList) {
            File file = new File(uploadFolder + "/" + fileobj.getSavefile());
            if (file.exists()) { // 해당 파일이 존재하면
                file.delete(); // 파일 삭제
            }
        }
        //데이터베이스의 파일 자료실과 파일의 내용 삭제
        int ck = productService.removeFileboard(postNo);
        return "redirect:/common/productList";
    }

    // 거래글 수정폼 이동
    @GetMapping("productUpdate")
    public String modifyFileboard(@RequestParam("no") Integer postNo, Model model) throws Exception {
        Product board = productService.getProduct(postNo);
        List<FileDTO> fileList = productService.getFileGroupList(postNo);
        model.addAttribute("board", board);
        model.addAttribute("fileList", fileList);
        return "/product/productUpdate";
    }

    // 중고거래글 수정
    @PostMapping("productUpdate")
    public String modifyFileboard2(@RequestParam("pno") Integer postNo,
                                   @RequestParam("files") List<MultipartFile> files,
                                   @RequestParam Map<String, String> params,
                                   HttpServletRequest req, Model model) throws Exception {

        FileVO fileboard = new FileVO();

        // Create the 'board' object
        Product board = new Product();
        board.setNo(postNo);
        board.setTitle(params.get("title"));
        board.setContent(params.get("content"));
        board.setCate(params.get("cate"));


        log.info("-----------------------------------");
        log.info(" 현재 프로젝트 홈 : " + req.getContextPath());
        log.info(" dispatcher-servlet에서 지정한 경로 : " + uploadFolder);
        log.info(" 요청 URL : " + req.getServletPath());
        log.info(" 프로젝트 저장 경로 : " + uploadFolder);
        //여러 파일 반복 저장
        List<FileDTO> fileList = new ArrayList<>();

        boolean checkFile = true;

        for (MultipartFile file : files) {
            if (!file.getOriginalFilename().isEmpty()) {

                // 파일 처리 로직 시작
                String randomUUID = UUID.randomUUID().toString();  // 파일 이름 중복 방지를 위한 랜덤 UUID 생성
                String OriginalFilename = file.getOriginalFilename();  // 실제 파일 이름
                String Extension = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));  // 파일 확장자 추출
                String saveFileName = randomUUID + Extension;  // 저장할 파일 이름 생성

                FileDTO data = new FileDTO();
                data.setSavefolder(uploadFolder);
                data.setOriginfile(file.getOriginalFilename());
                data.setSavefile(saveFileName);
                data.setFilesize(file.getSize());
                Date today = new Date();
                data.setUploaddate(today.toString());
                data.setPno(postNo);
                fileList.add(data);

                File saveFile = new File(uploadFolder, saveFileName); //실제 파일 객체 생성

                try {
                    file.transferTo(saveFile);  //실제 디렉토리에 해당파일 저장
//                file.transferTo(devFile); //개발자용 컴퓨터에 해당파일 저장
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                    // 예외 처리
                }
            } else {
                checkFile = false;
                break;
            }
        }
        
        if(checkFile) { // 파일이 있는 경우
            List<FileDTO> fileList2 = productService.getFileGroupList(postNo);
            for (FileDTO fileobj : fileList2) {
                File file = new File(uploadFolder + "/" + fileobj.getSavefile());
                if (file.exists()) { // 해당 파일이 존재하면
                    file.delete(); // 파일 삭제
                }
            }
            productService.removeFileAll(postNo);
            fileboard.setFileList(fileList); // 파일
            fileboard.setFileBoard(board); //글 제목 내용
            productService.updateFileboard(fileboard); // 모든 내용 업데이트
        } else { // 파일이 없는 경우
            productService.productUpdate(board); // 글 제목 내용만 업데이트
        }

        return "redirect:/common/getProduct?no=" + postNo;
    }

//    // 파일 삭제
//    @PostMapping("fileRemove")
//    @ResponseBody
//    public Boolean fileRemove(@RequestParam("no") Integer no, @RequestParam("pno") Integer postNo, HttpServletRequest req, Model model) throws Exception {
//        // 현재 작업 디렉토리를 가져와서 상대 경로를 만듭니다.
//        String currentWorkingDir = System.getProperty("user.dir");
//        String relativePath = File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "upload";
//
//        // 상대 경로와 업로드 디렉토리를 합쳐서 최종 경로를 만듭니다.
//        String path = currentWorkingDir + relativePath;
//        ;
//        FileDTO fileobj = productService.getFile(no);
//        int ck = productService.fileRemove(no);
//        boolean result = false;
//        if (ck == 1) {
//            result = false;
//        }
//        return result;
//    }

    // 내가 판 상품 목록
    // 상품 목록 보기
    @GetMapping("productList")
    public String myProductList(Principal principal, HttpServletRequest request, Model model) throws Exception {
        String category = request.getParameter("category");
        String type = request.getParameter("type");
        String keyword = request.getParameter("keyword");
        int curPage = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        Page page = new Page(curPage, type, keyword, category);
        page.setName(principal.getName());
        page.makePage(productService.getMyTotal(page));

        List<Product> productList = productService.myProductList(page);
        List<FileDTO> fileList = new ArrayList<>();
        for (Product pro:productList) {
            FileDTO dto = productService.thmbn(pro.getNo());
            fileList.add(dto);
        }

        model.addAttribute("productList", productList);
        model.addAttribute("fileList", fileList);
        model.addAttribute("page", page);
        return "user/myProductList";
    }
    
    // 거래확정
    @PostMapping("sale")
    public String sale(ChatRoom chatRoom, Model model) throws Exception {
        
        // product 구매처리
        Product product = new Product();
        product.setBuyer(chatRoom.getBuyer());
        product.setNo(chatRoom.getPno());
        int ck = productService.actUpdate(product);
        
        // 구매된 상품의 채팅방 dsable 처리
        int ck2 = chatService.actUpdate(chatRoom.getPno());
        
        return "redirect:/product/productList?name="+chatRoom.getSeller();
    }

    // 내가 구매한 상품 목록
    @GetMapping("productBuyerList")
    public String productBuyerList(Principal principal, HttpServletRequest request, Model model) throws Exception {
        String category = request.getParameter("category");
        String type = request.getParameter("type");
        String keyword = request.getParameter("keyword");
        int curPage = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        Page page = new Page(curPage, type, keyword, category);
        page.setName(principal.getName());
        page.makePage(productService.getBuyerTotal(page));

        List<Product> productList = productService.productBuyerList(page);
        List<FileDTO> fileList = new ArrayList<>();
        for (Product pro:productList) {
            FileDTO dto = productService.thmbn(pro.getNo());
            fileList.add(dto);
        }

        model.addAttribute("productList", productList);
        model.addAttribute("fileList", fileList);
        model.addAttribute("page", page);
        return "user/productBuyerList";
    }
}
