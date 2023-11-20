package com.chunjae.edumarket.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CalendarInfo {

    private int month;
    private int startDayOfWeek;         // 시작 요일
    private int endDay;                 // 달의 마지막 날짜

    public void setCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.clear();

        Date date = new Date();
        cal.set(date.getYear()+1900, date.getMonth(), 1);

        this.month = date.getMonth() + 1;
        this.startDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;        // 이번 달의 시작 요일
        this.endDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);      // 이번 달의 마지막 날짜
    }

}
