package com.chunjae.edumarket.ctrl;

import com.chunjae.edumarket.biz.FreeServiceImpl;
import com.chunjae.edumarket.biz.UserService;
import com.chunjae.edumarket.entity.Free;
import com.chunjae.edumarket.entity.FreeComment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/free/*")
public class FreeController {

    @Value("${spring.servlet.multipart.location}")
    String uploadFolder;

    @Autowired
    private FreeServiceImpl freeService;

    @Autowired
    private UserService userService;

    @GetMapping("freeList")
    public String freeList(Model model) throws Exception{
        List<Free> freeList = freeService.getFreeList();
        model.addAttribute("freeList", freeList);
        return "free/freeList";
    }

    @GetMapping("freeDetail")
    public String freeDetail(@RequestParam("no") Integer no, Model model) throws Exception{
        freeService.visitCount(no);
        Free free = freeService.getFree(no);
        List<FreeComment> freeCommentList = freeService.commentList(no); // 댓글 목록 불러오기
        log.info(free.toString());
        if(free==null) { // 회원이 없으면 예외처리, url로 직접 들어오는 것도 방지
            throw new NoSuchFieldException("No Such Data");
        }
        model.addAttribute("comment", freeCommentList); // 댓글 불러오기 !
        model.addAttribute("free", free);
        return "free/freeDetail";
    }

    @GetMapping("freeInsert")
    public String freeInsertForm(Principal principal, Model model) throws Exception{
//        Euser euser = userService.getUserById(Integer.valueOf(principal.getName()));
//        System.out.println(principal.getName());
        model.addAttribute("name", principal.getName());
        return "free/freeInsert";
    }

    @PostMapping("freeInsert")
    public String freeInsert(Free free, Model model) throws Exception{
        Integer ck = freeService.insertFree(free);
        if(ck == 1) {
            log.info("게시글 작성 성공");
            return "redirect:/free/freeList";
        } else {
            log.info("게시글 작성 실패");
            return "redirect:/";
        }
    }

    @GetMapping("freeEdit")
    public String freeEditForm(@RequestParam("no") Integer no, Model model) throws Exception{
        Free free = freeService.getFree(no);
        model.addAttribute("free", free);
        return "free/freeEdit";
    }

    @PostMapping("freeEdit")
    public String freeEdit(Free free, Model model) throws Exception{
        Integer ck = freeService.updateFree(free);
        if(ck == 1) {
            log.info("게시글 수정 성공");
            return "redirect:/free/freeDetail?no="+free.getNo();
        } else {
            log.info("게시글 수정 실패");
            return "redirect:/";
        }
    }

    @GetMapping("freeDelete")
    public String freeDelete(@RequestParam("no") Integer no, Model model) throws Exception{
        Integer ck = freeService.deleteFree(no);
        if(ck == 1) {
            log.info("게시글 삭제 성공");
            return "redirect:/free/freeList";
        } else {
            log.info("게시글 삭제 실패");
            return "redirect:/";
        }
    }

    //ckeditor를 이용한 이미지 업로드
    @PostMapping(value = "imageUpload.do")
    public void imageUpload(HttpServletRequest request,
                            HttpServletResponse response, MultipartHttpServletRequest multiFile
            , @RequestParam MultipartFile upload) throws Exception {
        // 랜덤 문자 생성
        UUID uid = UUID.randomUUID();

        OutputStream out = null;
        PrintWriter printWriter = null;

        //인코딩
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        try {
            //파일 이름 가져오기
            String fileName = upload.getOriginalFilename();
            byte[] bytes = upload.getBytes();

            //이미지 경로 생성
            String ckUploadPath = uploadFolder + "\\free\\" + uid + "_" + fileName;
            File folder = new File(uploadFolder);
            System.out.println("path:" + uploadFolder);    // 이미지 저장경로 console에 확인
            //해당 디렉토리 확인
            if (!folder.exists()) {
                try {
                    folder.mkdirs(); // 폴더 생성
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }

            out = new FileOutputStream(new File(ckUploadPath));
            out.write(bytes);
            out.flush(); // outputStram에 저장된 데이터를 전송하고 초기화

            String callback = request.getParameter("CKEditorFuncNum");
            printWriter = response.getWriter();
            String fileUrl = "/free/ckImgSubmit.do?uid=" + uid + "&fileName=" + fileName; // 작성화면

            // 업로드시 메시지 출력
            printWriter.println("{\"filename\" : \"" + fileName + "\", \"uploaded\" : 1, \"url\":\"" + fileUrl + "\"}");
            printWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    //ckeditor를 이용한 서버에 전송된 이미지 뿌려주기
    @RequestMapping(value = "ckImgSubmit.do")
    public void ckSubmit(@RequestParam(value = "uid") String uid
            , @RequestParam(value = "fileName") String fileName
            , HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //서버에 저장된 이미지 경로
        String path = uploadFolder + "\\free\\";
        String sDirPath = path + uid + "_" + fileName;

        File imgFile = new File(sDirPath);

        //사진 이미지 찾지 못하는 경우 예외처리로 빈 이미지 파일을 설정한다.
        if (imgFile.isFile()) {
            byte[] buf = new byte[1024];
            int readByte = 0;
            int length = 0;
            byte[] imgBuf = null;

            FileInputStream fileInputStream = null;
            ByteArrayOutputStream outputStream = null;
            ServletOutputStream out = null;

            try {
                fileInputStream = new FileInputStream(imgFile);
                outputStream = new ByteArrayOutputStream();
                out = response.getOutputStream();

                while ((readByte = fileInputStream.read(buf)) != -1) {
                    outputStream.write(buf, 0, readByte);
                }

                imgBuf = outputStream.toByteArray();
                length = imgBuf.length;
                out.write(imgBuf, 0, length);
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                outputStream.close();
                fileInputStream.close();
                out.close();
            }
        }
    }

    // 댓글

    @PostMapping("commentInsert")
    public String insertComment(FreeComment freeComment) throws Exception{
        log.info(freeComment.toString());
        Integer ck = freeService.insertFreeComment(freeComment);
        if(ck == 1) {
            log.info("댓글 작성 성공");
            return "redirect:/free/freeDetail?no="+freeComment.getPar();
        } else {
            log.info("댓글 작성 실패");
            return "redirect:/";
        }
    }

    @GetMapping("commentUpdate")
    public String updateCommentGet(@RequestParam("no") Integer no, Model model) throws Exception{
        FreeComment freeComment = freeService.getFreeComment(no);
        model.addAttribute("freeComment", freeComment);
        return "free/updateComment";
    }

    @PostMapping("commentUpdate")
    public String updateCommentPost(@RequestParam("no") Integer no, @RequestParam("content") String content, Model model){
        FreeComment freeComment = freeService.getFreeComment(no);
        freeComment.setNo(no);
        freeComment.setContent(content);
        int ck = freeService.updateFreeComment(freeComment);
        return "redirect:/free/freeDetail?no="+freeComment.getPar();
    }

    @GetMapping("commentDelete")
    public String deleteComment(@RequestParam("no") Integer no, Model model){
        FreeComment freeComment = freeService.getFreeComment(no);
        int ck = freeService.deleteFreeComment(no);
        return "redirect:/free/freeDetail?no="+freeComment.getPar();
    }
}
