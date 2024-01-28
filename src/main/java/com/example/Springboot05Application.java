package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName Springboot05Application
 * @Description 启动类
 * @Author pxh
 * @Date 2024/1/19 15:17
 * @Version 1.0
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.example.mapper")
public class Springboot05Application {
    public static void main(String[] args) {
        SpringApplication.run(Springboot05Application.class, args);
    }
}
