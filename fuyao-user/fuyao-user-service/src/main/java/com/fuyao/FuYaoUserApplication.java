package com.fuyao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.fuyao.user.mapper")
public class FuYaoUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(FuYaoUserApplication.class,args);
    }
}
