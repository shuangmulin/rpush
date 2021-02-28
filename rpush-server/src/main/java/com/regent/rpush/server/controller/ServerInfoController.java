package com.regent.rpush.server.controller;

import com.regent.rpush.api.server.ServerInfoService;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.rpushserver.ServerInfoDTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器信息
 *
 * @author 钟宝林
 * @since 2021/2/26/026 21:35
 **/
@RestController
@RequestMapping("/server-info")
public class ServerInfoController implements ServerInfoService {

    @Autowired
    private ServerInfoDTO serverInfo;

    @SneakyThrows
    @GetMapping
    public ApiResult<ServerInfoDTO> serverInfo() {
        return ApiResult.of(serverInfo);
    }

}
