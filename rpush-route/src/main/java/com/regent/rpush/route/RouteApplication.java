package com.regent.rpush.route;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableSwagger2Doc
@SpringBootApplication(scanBasePackages = "com.regent.rpush")
@EnableFeignClients(basePackages = "com.regent.rpush.api.server")
@MapperScan(basePackages = "com.regent.rpush.route.mapper")
@EnableDiscoveryClient
public class RouteApplication {

    public static void main(String[] args) {
        SpringApplication.run(RouteApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfig() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true)
                        .maxAge(3600)
                        .allowedHeaders("*");
            }
        };
    }

}
