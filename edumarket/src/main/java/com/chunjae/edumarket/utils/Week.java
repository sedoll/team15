package com.chunjae.edumarket.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Week {
    public List<String> getDate() {
        // 오늘 날짜 추출
        LocalDate today = LocalDate.now();
        List<String> datesOfWeek = new ArrayList<>();

        // 이번 주의 월요일과 금요일 날짜 계산
        LocalDate monday = today;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }

        LocalDate friday = today;
        while (friday.getDayOfWeek() != DayOfWeek.FRIDAY) {
            friday = friday.plusDays(1);
        }

        // 월요일부터 금요일까지의 날짜를 리스트에 저장
        LocalDate tempDate = monday;
        while (!tempDate.isAfter(friday)) {
            datesOfWeek.add(tempDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            tempDate = tempDate.plusDays(1);
        }

        return datesOfWeek;
    }
}
