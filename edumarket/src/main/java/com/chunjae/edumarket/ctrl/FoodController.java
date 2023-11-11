package com.chunjae.edumarket.ctrl;

import com.chunjae.edumarket.biz.FoodService;
import com.chunjae.edumarket.entity.School;
import com.chunjae.edumarket.utils.Week;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class FoodController {
    @Autowired
    private FoodService foodService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
            logger.info(dto.toString());
            System.out.println(dto.toString());

            String codeS = dto.getSc_code();
            String codeK = dto.getEo_code();
            String schoolName = dto.getSc_name();

//            String codeS = "7041136";
//            String codeK = "B10";
//            String schoolName = "서울신구로초등학교";

            int minValue = 1;
            int maxValue = 5;

            foodService.menuServiceSet(codeS, codeK, date, minValue, maxValue);
            ddishList = foodService.getDdishList();
            mlsvList = foodService.getMlsvList();
            orplcList = foodService.getOrplcList();
            calList = foodService.getCalList();
            ntrList = foodService.getNtrList();

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
