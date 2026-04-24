package com.digitalpark;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 智慧园区综合管理平台 - 主启动类
 *
 * @author digitalpark
 */
@EnableAsync
@EnableScheduling
@SpringBootApplication
@MapperScan("com.digitalpark.modules.*.mapper")
public class DigitalParkApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalParkApplication.class, args);
        System.out.println("========================================");
        System.out.println("  智慧园区综合管理平台启动成功!");
        System.out.println("  接口文档地址: http://localhost:8080/doc.html");
        System.out.println("========================================");
    }
}
