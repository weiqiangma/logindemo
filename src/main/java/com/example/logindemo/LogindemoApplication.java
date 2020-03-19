package com.example.logindemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication(scanBasePackages = "com")
@MapperScan(basePackages = "com.example.logindemo.mapper.")
public class LogindemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogindemoApplication.class, args);
    }

}
