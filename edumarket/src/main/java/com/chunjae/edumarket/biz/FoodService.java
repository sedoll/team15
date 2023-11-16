package com.chunjae.edumarket.biz;

import com.chunjae.edumarket.api.MenuKey;
import com.chunjae.edumarket.entity.School;
import com.chunjae.edumarket.per.FoodMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FoodService {
    @Autowired
    private FoodMapper foodMapper;

    public School getSchool(String scname) {
        System.out.println("data: %"+scname+"%");
        System.out.println("mapper: " + foodMapper.getSchool("%"+scname+"%"));
        return foodMapper.getSchool(scname);
    }

    private List<String> ddishList = new ArrayList<>(); // 식단
    private List<String> orplcList = new ArrayList<>(); // 원산지
    private List<String> calList = new ArrayList<>(); // 칼로리
    private List<String> ntrList = new ArrayList<>(); // 영양

    public void menuServiceSet(String codeS, String codeK, List<String> date, int minValue, int maxValue) {
        Map<String, String> result = new HashMap<>(); // 결과

        MenuKey key = new MenuKey(); // api 키

        try {
            // API 엔드포인트 URL
            String apiUrl = "https://open.neis.go.kr/hub/mealServiceDietInfo?KEY=" + key.getKey() + "&Type=xml&pIndex=" + minValue +
                    "&pSize=" + maxValue + "&ATPT_OFCDC_SC_CODE=" + codeK + "&SD_SCHUL_CODE=" + codeS + "&MLSV_FROM_YMD=" + date.get(0) + "&MLSV_TO_YMD=" + date.get(4);
            System.out.println(apiUrl);
            // API 요청 보내기
            Document doc = Jsoup.connect(apiUrl).get();

            // ORPLC_INFO 및 MLSV_YMD 태그의 내용 추출
            Elements ddishNmElements = doc.select("DDISH_NM");
            Elements mlsvYmdElements = doc.select("MLSV_YMD");
            Elements orplcElements = doc.select("ORPLC_INFO");
            Elements calElements = doc.select("CAL_INFO");
            Elements ntrElements = doc.select("NTR_INFO");

            ddishList.clear();
            // DDISH_NM 데이터 출력
            for (Element element : ddishNmElements) {
                String ddishNmData = element.text();
                System.out.println("DDISH_NM 데이터: " + ddishNmData);
                ddishList.add(ddishNmData);
            }

            orplcList.clear();
            // ORPLC_INFO 데이터 출력
            for (Element element : orplcElements) {
                String data = element.text();
                System.out.println("ORPLC_INFO 데이터: " + data);
                orplcList.add(data);
            }

            calList.clear();
            // CAL_INFO 데이터 출력
            for (Element element : calElements) {
                String data = element.text();
                System.out.println("CAL_INFO 데이터: " + data);
                calList.add(data);
            }

            ntrList.clear();
            // NTR_INFO 데이터 출력
            for (Element element : ntrElements) {
                String data = element.text();
                System.out.println("NTR_INFO 데이터: " + data);
                ntrList.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getDdishList() {
        return ddishList;
    }

    public List<String> getOrplcList() {
        return orplcList;
    }

    public List<String> getCalList() {
        return calList;
    }

    public List<String> getNtrList() {
        return ntrList;
    }
}
