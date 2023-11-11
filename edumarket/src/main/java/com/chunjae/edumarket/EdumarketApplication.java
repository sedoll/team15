package com.chunjae.edumarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class EdumarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdumarketApplication.class, args);
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }
}
