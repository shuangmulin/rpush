package com.regent.rpush.server;

import com.regent.rpush.dto.rpushserver.ServerInfoDTO;
import com.spring4all.swagger.EnableSwagger2Doc;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableSwagger2Doc
@SpringBootApplication(scanBasePackages = "com.regent.rpush.server")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.regent.rpush.api.route")
public class ServerApplication {

    @Value("${server.port}")
    private int httpPort;

    @Value("${rpush.server.port}")
    private int socketPort;

    @Value("${rpush.web-socket.port}")
    private int webSocketPort;

    @Value("${rpush.ip}")
    private String ip;

    @Autowired
    private InetUtils inetUtils;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @SneakyThrows
    @Bean
    public ServerInfoDTO serverInfo() {
        InetUtils.HostInfo hostInfo = inetUtils.findFirstNonLoopbackHostInfo();
        String host = ip;
        if (StringUtils.equals(host, "none")) {
            host = hostInfo.getIpAddress();
        }
        return ServerInfoDTO.builder().httpPort(httpPort).socketPort(socketPort).webSocketPort(webSocketPort).host(host).build();
    }

}
