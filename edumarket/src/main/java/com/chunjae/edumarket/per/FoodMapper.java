package com.chunjae.edumarket.per;

import com.chunjae.edumarket.entity.School;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FoodMapper {
    School getSchool(String sc_name);
}
