package com.regent.rpush.route;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableSwagger2Doc
@SpringBootApplication(scanBasePackages = "com.regent.rpush")
@EnableFeignClients(basePackages = "com.regent.rpush")
@MapperScan(basePackages = "com.regent.rpush.route.mapper")
@EnableDiscoveryClient
public class RouteApplication {

    public static void main(String[] args) {
        SpringApplication.run(RouteApplication.class, args);
    }

}
