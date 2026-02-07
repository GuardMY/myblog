package com.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@MapperScan("com.blog.dao.mapper")
public class BlogBackendApplication {
    private static final Logger log = LoggerFactory.getLogger(BlogBackendApplication.class);
    
    public static void main(String[] args) {
        try {
            SpringApplication.run(BlogBackendApplication.class, args);
        } catch (Exception e) {
            log.error("", e);
        }
        log.info("----------------Blog Backend Application Started------------------");
    }
}