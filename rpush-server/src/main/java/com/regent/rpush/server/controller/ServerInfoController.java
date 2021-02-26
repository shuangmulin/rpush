package com.regent.rpush.server.controller;

import com.regent.rpush.api.server.ServerInfoService;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.rpushserver.ServerInfoDTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

/**
 * 服务器信息
 *
 * @author 钟宝林
 * @since 2021/2/26/026 21:35
 **/
@RestController
@RequestMapping("/server-info")
public class ServerInfoController implements ServerInfoService {

    @Value("${server.port}")
    private int httpPort;

    @Value("${rpush.server.port}")
    private int socketPort;

    @SneakyThrows
    @GetMapping
    public ApiResult<ServerInfoDTO> serverInfo() {
        String host = InetAddress.getLocalHost().getHostAddress();
        return ApiResult.of(ServerInfoDTO.builder().httpPort(httpPort).socketPort(socketPort).host(host).build());
    }

}
