package com.chunjae.edumarket.ctrl;

import com.chunjae.edumarket.biz.FoodService;
import com.chunjae.edumarket.entity.School;
import com.chunjae.edumarket.utils.Week;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/food/*")
@Log4j2
public class FoodController {
    @Autowired
    private FoodService foodService;

    @GetMapping("foodList")		// board/list.do
    public String boardList(Model model) throws Exception {
        return "food/foodList";
    }

    @PostMapping("foodList")		// board/list.do
    public String getBoardList(@RequestParam("name") String sc_name, Model model) {
        try {
            System.out.println(sc_name);

            List<String> ddishList; // 식단
            List<String> mlsvList; // 날짜
            List<String> orplcList; // 원산지
            List<String> calList; // 칼로리
            List<String> ntrList; // 영양

            Week week = new Week();

            List<String> date = week.getDate(); // 날짜 정보 갖고오기

            School dto = foodService.getSchool(sc_name);
            log.info(dto.toString());
            System.out.println(dto.toString());

            String codeS = dto.getSccode();
            String codeK = dto.getEocode();
            String schoolName = dto.getScname();

//            String codeS = "7041136";
//            String codeK = "B10";
//            String schoolName = "서울신구로초등학교";

            int minValue = 1;
            int maxValue = 5;

            foodService.menuServiceSet(codeS, codeK, date, minValue, maxValue);
            mlsvList = date;
            ddishList = foodService.getDdishList();
            orplcList = foodService.getOrplcList();
            calList = foodService.getCalList();
            ntrList = foodService.getNtrList();

            while (ddishList.size() < 5) {
                ddishList.add("정보없음");
            }
            while (orplcList.size() < 5) {
                orplcList.add("정보없음");
            }
            while (calList.size() < 5) {
                calList.add("정보없음");
            }
            while (ntrList.size() < 5) {
                ntrList.add("정보없음");
            }

            model.addAttribute("schoolName", schoolName);
            model.addAttribute("ddishList", ddishList);
            model.addAttribute("mlsvList", mlsvList);
            model.addAttribute("orplcList", orplcList);
            model.addAttribute("calList", calList);
            model.addAttribute("ntrList", ntrList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return "food/foodList";
        }
    }
}
