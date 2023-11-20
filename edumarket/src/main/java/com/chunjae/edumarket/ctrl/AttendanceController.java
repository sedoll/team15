package com.chunjae.edumarket.ctrl;

import com.chunjae.edumarket.biz.AttendanceService;
import com.chunjae.edumarket.utils.CalendarInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/attendance/*")
public class AttendanceController {

    @Autowired
    HttpSession session;

    @Autowired
    AttendanceService attendanceService;

    @GetMapping("check")
    public String attendanceCheck(Principal principal, Model model) throws Exception {
        String id = principal.getName();

        List<Integer> attendanceList = attendanceService.attendanceList(id);
        boolean attendChk = attendanceService.isAttendance(id);
        List<String> givawayList = attendanceService.givawayList(id);

        CalendarInfo calendarInfo = new CalendarInfo();
        calendarInfo.setCalendar();

        model.addAttribute("list", attendanceList);
        model.addAttribute("givawayList", givawayList);
        model.addAttribute("attendChk", attendChk);         // 오늘 출석체크 했는지 체크
        model.addAttribute("calendarInfo", calendarInfo);

        return "attendance/check";
    }

    @GetMapping("addAttend")
    public String addAttend(Principal principal, RedirectAttributes rttr) throws Exception {
        String givaway = attendanceService.addAttend(principal.getName());

        rttr.addFlashAttribute("givaway", givaway);
        return "redirect:/attendance/check";
    }

}
