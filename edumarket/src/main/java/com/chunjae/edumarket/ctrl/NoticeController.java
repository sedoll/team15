package com.chunjae.edumarket.ctrl;

import com.chunjae.edumarket.biz.NoticeServiceImpl;
import com.chunjae.edumarket.entity.Notice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@CrossOrigin("http://localhost:8085")
@RequestMapping("/notice/*")
public class NoticeController {

    @Autowired
    private NoticeServiceImpl noticeService;

    @GetMapping("noticeList")
    public String noticeList(Model model) throws Exception{
        List<Notice> noticeList = noticeService.getNoticeList();
        model.addAttribute("noticeList", noticeList);
        return "notice/noticeList";
    }

    @GetMapping("noticeDetail")
    public String noticeDetail(@RequestParam("no") Integer no, Model model) throws Exception{
        noticeService.visitCount(no);
        Notice notice = noticeService.getNotice(no);
        log.info(notice.toString());
        if(notice==null) { // 회원이 없으면 예외처리, url로 직접 들어오는 것도 방지
            throw new NoSuchFieldException("No Such Data");
        }
        model.addAttribute("notice", notice);
        return "notice/noticeDetail";
    }

    @GetMapping("noticeInsert")
    public String noticeInsertForm(Model model) throws Exception{
        return "notice/noticeInsert";
    }

    @PostMapping("noticeInsert")
    public String noticeInsert(Notice notice, Model model) throws Exception{
        Integer ck = noticeService.insertNotice(notice);
        if(ck == 1) {
            log.info("게시글 작성 성공");
            return "redirect:/notice/noticeList";
        } else {
            log.info("게시글 작성 실패");
            return "redirect:/";
        }
    }

    @GetMapping("noticeEdit")
    public String noticeEditForm(@RequestParam("no") Integer no, Model model) throws Exception{
        Notice notice = noticeService.getNotice(no);
        model.addAttribute("notice", notice);
        return "notice/noticeEdit";
    }

    @PostMapping("noticeEdit")
    public String noticeEdit(Notice notice, Model model) throws Exception{
        Integer ck = noticeService.updateNotice(notice);
        if(ck == 1) {
            log.info("게시글 수정 성공");
            return "redirect:/notice/noticeDetail?no="+notice.getNo();
        } else {
            log.info("게시글 수정 실패");
            return "redirect:/";
        }
    }

    @GetMapping("noticeDelete")
    public String noticeDelete(@RequestParam("no") Integer no, Model model) throws Exception{
        Integer ck = noticeService.deleteNotice(no);
        if(ck == 1) {
            log.info("게시글 삭제 성공");
            return "redirect:/notice/noticeList";
        } else {
            log.info("게시글 삭제 실패");
            return "redirect:/";
        }
    }

    //ckeditor를 이용한 이미지 업로드
    @RequestMapping(value = "imageUpload.do", method = RequestMethod.POST)
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
            String path = "E:\\edumarket_5차\\src\\main\\webapp\\resources\\upload" + "ckImage/";    // 이미지 경로 설정(폴더 자동 생성)
            String ckUploadPath = path + uid + "_" + fileName;
            File folder = new File(path);
            System.out.println("path:" + path);    // 이미지 저장경로 console에 확인
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
            String fileUrl = "/notice/ckImgSubmit.do?uid=" + uid + "&fileName=" + fileName; // 작성화면

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
        String path = "E:\\edumarket_5차\\src\\main\\webapp\\resources\\upload" + "ckImage/";    // 저장된 이미지 경로
        System.out.println("path:" + path);
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
}
