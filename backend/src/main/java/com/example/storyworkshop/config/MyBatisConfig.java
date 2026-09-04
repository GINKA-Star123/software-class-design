package com.example.storyworkshop.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.example.storyworkshop.module")
public class MyBatisConfig {
}