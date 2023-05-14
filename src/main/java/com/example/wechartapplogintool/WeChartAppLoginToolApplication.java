package com.example.wechartapplogintool;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.example.wechartapplogintool.mapper"})
public class WeChartAppLoginToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeChartAppLoginToolApplication.class, args);
    }

}
