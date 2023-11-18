package com.chunjae.edumarket.biz;

import com.chunjae.edumarket.entity.Attendance;

import java.util.List;

public interface AttendanceService {
    public List<Integer> attendanceList(String id) throws Exception;
    public List<String> givawayList(String id) throws Exception;
    public boolean isAttendance(String id) throws Exception;
    public String addAttend(String id) throws Exception;
    public List<Attendance> attendaceDetail(String id) throws Exception;
}
