package com.chunjae.edumarket;

import com.chunjae.edumarket.biz.ProductServiceImpl;
import com.chunjae.edumarket.entity.FileDTO;
import com.chunjae.edumarket.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class EdumarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdumarketApplication.class, args);
    }

    @Autowired
    private ProductServiceImpl productService;
    
    // index
    @GetMapping("/")
    public String home(Model model) throws Exception {
        List<Product> productList = productService.productList();
        List<FileDTO> fileList = new ArrayList<>();
        for (Product pro:productList) {
            FileDTO dto = productService.thmbn(pro.getNo());
            fileList.add(dto);
        }
//        log.info(fileboardList.toString());
        model.addAttribute("productList", productList);
        model.addAttribute("fileList", fileList);

        return "index";
    }
}
