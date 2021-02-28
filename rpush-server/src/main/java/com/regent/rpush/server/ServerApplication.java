package com.regent.rpush.server;

import com.regent.rpush.dto.rpushserver.ServerInfoDTO;
import com.spring4all.swagger.EnableSwagger2Doc;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.net.InetAddress;

@EnableSwagger2Doc
@SpringBootApplication(scanBasePackages = "com.regent.rpush.server")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.regent.rpush.api.route")
public class ServerApplication {

    @Value("${server.port}")
    private int httpPort;

    @Value("${rpush.server.port}")
    private int socketPort;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @SneakyThrows
    @Bean
    public ServerInfoDTO serverInfo() {
        String host = InetAddress.getLocalHost().getHostAddress();
        return ServerInfoDTO.builder().httpPort(httpPort).socketPort(socketPort).host(host).build();
    }

}
