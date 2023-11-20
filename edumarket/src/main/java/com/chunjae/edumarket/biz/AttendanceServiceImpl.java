package com.chunjae.edumarket.biz;

import com.chunjae.edumarket.entity.Attendance;
import com.chunjae.edumarket.per.AttendanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Override
    public List<Integer> attendanceList(String id) throws Exception {
        return attendanceMapper.attendanceList(id);
    }

    @Override
    public List<String> givawayList(String id) throws Exception {
        return attendanceMapper.givawayList(id);
    }

    @Override
    public boolean isAttendance(String id) throws Exception {
        boolean result = false;
        if(attendanceMapper.getAttendance(id) != null) {
            result = true;
        }
        return result;
    }

    @Override
    public String addAttend(String id) throws Exception {
        // 랜덤 경품 뽑기
        double tmpRandom = Math.round(Math.random() * 100) / 100.0;     // 소수 둘째자리까지 절삭
        double tmpRatePrev = 0;
        double tmpRateNext = 0;
        String givaway = "001.png";

        HashMap<String, String> map = new HashMap<String, String>();
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        map = new HashMap<String, String>();

        map.put("rate", "25");
        map.put("value", "001.png");
        list.add(map);
        map = new HashMap<String, String>();

        map.put("rate", "25");
        map.put("value", "001.png");
        list.add(map);
        map = new HashMap<String, String>();

        map.put("rate", "25");
        map.put("value", "001.png");
        list.add(map);
        map = new HashMap<String, String>();

        map.put("rate", "10");
        map.put("value", "002.png");
        list.add(map);
        map = new HashMap<String, String>();

        map.put("rate", "5");
        map.put("value", "003.png");
        list.add(map);
        map = new HashMap<String, String>();

        map.put("rate", "4");
        map.put("value", "004.png");
        list.add(map);
        map = new HashMap<String, String>();

        map.put("rate", "3");
        map.put("value", "005.png");
        list.add(map);
        map = new HashMap<String, String>();

        map.put("rate", "2");
        map.put("value", "006.png");
        list.add(map);
        map = new HashMap<String, String>();

        map.put("rate", "1");
        map.put("value", "007.png");
        list.add(map);
        map.clear();
        map = new HashMap<String, String>();

        for(int i=0; i<list.size(); i++) {
            if(tmpRandom == 100) {
                givaway = list.get(list.size() - 1).get("value");
                break;
            } else {
                double rate = Double.parseDouble(list.get(i).get("rate"));
                tmpRateNext = tmpRatePrev + rate;
                if(tmpRandom >= tmpRatePrev && tmpRandom < tmpRateNext) {
                    givaway = list.get(i).get("value");
                    break;
                } else {
                    tmpRatePrev = tmpRateNext;
                }
            }
        }
        System.out.println(givaway + "에 당첨되었습니다.");

        // 출석체크 정보 저장
        Attendance attendance = new Attendance(id, givaway);
        attendanceMapper.addAttend(attendance);

        return givaway;
    }

    @Override
    public List<Attendance> attendaceDetail(String id) throws Exception {
        return attendanceMapper.attendaceDetail(id);
    }
}
