package com.chunjae.edumarket.per;

import com.chunjae.edumarket.entity.Faq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FaqMapper {
    public List<Faq> faqList() throws Exception;
}
