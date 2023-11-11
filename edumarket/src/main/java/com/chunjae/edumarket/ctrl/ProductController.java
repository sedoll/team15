package com.chunjae.edumarket.ctrl;

import com.chunjae.edumarket.biz.ProductServiceImpl;
import com.chunjae.edumarket.entity.FileDTO;
import com.chunjae.edumarket.entity.FileVO;
import com.chunjae.edumarket.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Controller
@CrossOrigin("http://localhost:8085")
@RequestMapping("/product/*")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    // 게시글 입력 폼 이동
    // 일치하는 데이터가 없으면 null 반환
    @GetMapping("productInsert")
    public String productInsertForm(@RequestParam("name") String name, Model model) throws Exception {
        model.addAttribute("name", name);
        return "product/productInsert";
    }

    // 게시글 수정 폼 이동
    // 일치하는 데이터가 없으면 null 반환
    @GetMapping("productUpdate")
    public String productUpdateForm(@RequestParam("no") Integer no, Model model) throws Exception {
        Product product = productService.getProduct(no);
        model.addAttribute("product", product);
        return "product/productUpdate";
    }

    // 게시글 수정
    // 일치하는 데이터가 없으면 null 반환
    @PostMapping ("productUpdate")
    public String productUpdate(Product product, Model model) throws Exception {
        Integer ck = productService.updatProduct(product);
        if(ck == 1) {
            log.info("게시글 수정 성공");
            return "redirect:/common/getProduct?id="+product.getId();
        } else {
            log.info("게시글 수정 실패");
            return "redirect:/";
        }
    }
    
    // 중고거래 추가 폼이동
    @GetMapping("fileUpload")
    public String fileUploadForm(){
        return "product/productInsert";
    }
    
    // 중고거래 추가
    @PostMapping("fileUpload")
    public String fileUpload1(MultipartHttpServletRequest files, HttpServletRequest req, Model model) throws Exception {

        //파라미터 분리
        Enumeration<String> e = files.getParameterNames();
        Map map = new HashMap();
        while (e.hasMoreElements()) {
            String name = e.nextElement();
            String value = files.getParameter(name);
            map.put(name, value);
        }

        //제목 및 내용 분리
        Product board = new Product();
        board.setId((String) map.get("id"));
        board.setTitle((String) map.get("title"));
        board.setContent((String) map.get("content"));
        board.setAddr((String) map.get("addr"));
        // 현재 작업 디렉토리를 가져와서 상대 경로를 만듭니다.
        String currentWorkingDir = System.getProperty("user.dir");
        String relativePath = File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "upload";

        // 상대 경로와 업로드 디렉토리를 합쳐서 최종 경로를 만듭니다.
        String uploadFolder = currentWorkingDir + relativePath;;
        File folder = new File(uploadFolder);
        if(!folder.exists())
            folder.mkdirs();
        log.info("-----------------------------------");
        log.info(" 현재 프로젝트 홈 : "+ req.getContextPath());
        log.info(" 지정한 경로 : "+uploadFolder);
        log.info(" 요청 URL : "+req.getServletPath());
        log.info(" 프로젝트 저장 경로 : "+uploadFolder);

        //여러 파일 반복 저장
        List<FileDTO> fileList = new ArrayList<>();
        Iterator<String> it = files.getFileNames();
        while(it.hasNext()){
            String paramfName = it.next();
            MultipartFile file = files.getFile(paramfName);
            log.info("-----------------------------------");
            log.info("name : "+file.getOriginalFilename());
            log.info("size : "+file.getSize());
            log.info("path : ");

            if(!file.getOriginalFilename().equals("")) { // 빈 파일은 업로드 안함
                
                String randomUUID = UUID.randomUUID().toString(); // 파일 이름 중복 방지를 위한 랜덤 설정
                String OriginalFilename = file.getOriginalFilename(); // 실제 파일 이름
                log.info(OriginalFilename);
                String Extension = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));
                String saveFileName = randomUUID + Extension;

                FileDTO data = new FileDTO();
                data.setSavefolder(uploadFolder);
                data.setOriginfile(file.getOriginalFilename());
                data.setSavefile(saveFileName);
                data.setFilesize(file.getSize());
                Date today = new Date();
                data.setUploaddate(today.toString());
                fileList.add(data);

                File saveFile = new File(uploadFolder, saveFileName); //실제 파일 객체 생성

                try {
                    file.transferTo(saveFile);  //실제 디렉토리에 해당파일 저장
//                file.transferTo(devFile); //개발자용 컴퓨터에 해당파일 저장
                } catch(IllegalStateException e1){
                    log.info(e1.getMessage());
                } catch(IOException e2){
                    log.info(e2.getMessage());
                }
            }
        }
        FileVO fileboard = new FileVO();
        fileboard.setFileList(fileList);
        fileboard.setFileBoard(board);
        productService.insertFileboard(fileboard);
        return "redirect:/common/productList";
    }
    
    // 중고상품게시글 및 묶여있던 파일 삭제
    @GetMapping("productDelete")
    public String productDelete(@RequestParam("no") Integer postNo, HttpServletRequest req) throws Exception {

        //실제 파일 삭제 로직
        //파일 경로 지정
        // 현재 작업 디렉토리를 가져와서 상대 경로를 만듭니다.
        String currentWorkingDir = System.getProperty("user.dir");
        String relativePath = File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "upload";

        // 상대 경로와 업로드 디렉토리를 합쳐서 최종 경로를 만듭니다.
        String path = currentWorkingDir + relativePath;;
//        String path = req.getRealPath("/static/upload");
        List<FileDTO> fileList = productService.getFileGroupList(postNo);
        for(FileDTO fileobj : fileList) {
            File file = new File(path + "/" + fileobj.getOriginfile());
            if (file.exists()) { // 해당 파일이 존재하면
                file.delete(); // 파일 삭제
            }
        }
        //데이터베이스의 파일 자료실과 파일의 내용 삭제
        int ck = productService.removeFileboard(postNo);
        return "redirect:/common/productList";
    }

    @GetMapping("modifyFileboard")
    public String modifyFileboard(@RequestParam("no") Integer postNo, Model model) throws Exception {
        Product board = productService.getProduct(postNo);
        List<FileDTO> fileList = productService.getFileGroupList(postNo);
        model.addAttribute("board", board);
        model.addAttribute("fileList", fileList);
        return "/product/productUpdate";
    }

    @PostMapping("modifyFileboard")
    public String modifyFileboard2(@RequestParam("pno") Integer postNo, MultipartHttpServletRequest files, HttpServletRequest req,Model model) throws Exception {
        FileVO fileboard = new FileVO();
//        model.addAttribute("fileboard", fileboard);
        /////////////
        //파라미터 분리
        Enumeration<String> e = files.getParameterNames();
        Map map = new HashMap();
        while (e.hasMoreElements()) {
            String name = e.nextElement();
            String value = files.getParameter(name);
            map.put(name, value);
            System.out.println("map : "+map.toString());
        }
        //제목 및 내용 분리
        Product board = new Product();
        board.setNo(postNo);
        board.setTitle((String) map.get("title"));
        board.setContent((String) map.get("content"));


        // 현재 작업 디렉토리를 가져와서 상대 경로를 만듭니다.
        String currentWorkingDir = System.getProperty("user.dir");
        String relativePath = File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "upload";

        // 상대 경로와 업로드 디렉토리를 합쳐서 최종 경로를 만듭니다.
        String uploadFolder = currentWorkingDir + relativePath;;
        log.info("-----------------------------------");
        log.info(" 현재 프로젝트 홈 : "+req.getContextPath());
        log.info(" dispatcher-servlet에서 지정한 경로 : "+uploadFolder);
        log.info(" 요청 URL : "+req.getServletPath());
        log.info(" 프로젝트 저장 경로 : "+uploadFolder);
        //여러 파일 반복 저장
        List<FileDTO> fileList = new ArrayList<>();
        Iterator<String> it = files.getFileNames();

        while(it.hasNext()){
            String paramfName = it.next();
            MultipartFile file = files.getFile(paramfName);
            log.info("-----------------------------------");
            log.info("name : "+file.getOriginalFilename());
            log.info("size : "+file.getSize());
            log.info("path : ");

            if(!file.getOriginalFilename().equals("")) {
                String randomUUID = UUID.randomUUID().toString(); // 파일 이름 중복 방지를 위한 랜덤 설정
                String OriginalFilename = file.getOriginalFilename(); // 실제 파일 이름
                log.info(OriginalFilename);
                String Extension = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));
                String saveFileName = randomUUID + Extension;

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
                } catch(IllegalStateException e1){
                    log.info(e1.getMessage());
                } catch(IOException e2){
                    log.info(e2.getMessage());
                }

//                if (!fileList.isEmpty()) {
                if (!file.getOriginalFilename().equals("")) {
                    productService.removeFileAll(postNo);
                }
            }
            productService.updateFileboard(fileboard);
        }

        fileboard.setFileList(fileList);
        fileboard.setFileBoard(board); //글 제목 내용
//        productService.removeFileAll(postNo);
        productService.updateFileboard(fileboard);
        /////////////
        return "redirect:/product/getFileboard?pno="+postNo;
    }


    @PostMapping("fileRemove")
    public String fileRemove(@RequestParam("no") Integer no, @RequestParam("pno") Integer postNo, HttpServletRequest req, Model model) throws Exception {
        String path = req.getRealPath("/static/upload");
        FileDTO fileobj = productService.getFile(no);
        File file = new File(path + "/" + fileobj.getOriginfile());
        if (file.exists()) { // 해당 파일이 존재하면
            file.delete(); // 파일 삭제
            productService.fileRemove(no);
            log.info("file delete");
        }
        return "redirect:/product/getFileboard?pno="+postNo;
    }
}
