package com.chunjae.edumarket.biz;

import com.chunjae.edumarket.entity.Faq;
import com.chunjae.edumarket.per.FaqMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaqServiceImpl implements FaqService{

    @Autowired
    private FaqMapper faqMapper;

    @Override
    public List<Faq> faqList() throws Exception {
        return faqMapper.faqList();
    }
}
