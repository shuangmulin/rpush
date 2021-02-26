package com.regent.rpush.api.server;

import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.rpushserver.ServerInfoDTO;
import lombok.SneakyThrows;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "rpush-server", contextId = "ServerInfoService")
@RequestMapping("/server-info")
public interface ServerInfoService {

    /**
     * 返回对应服务器的信心
     */
    @SneakyThrows
    @GetMapping
    ApiResult<ServerInfoDTO> serverInfo();
}