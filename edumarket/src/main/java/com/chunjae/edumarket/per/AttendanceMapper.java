package com.chunjae.edumarket.per;

import com.chunjae.edumarket.entity.Attendance;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AttendanceMapper {
    List<Integer> attendanceList(String id) throws Exception;
    List<String> givawayList(String id) throws Exception;
    Attendance getAttendance(String id) throws Exception;
    void addAttend(Attendance attendance) throws Exception;
    List<Attendance> attendaceDetail(String id) throws Exception;
}
