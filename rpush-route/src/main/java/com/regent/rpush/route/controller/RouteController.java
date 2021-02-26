package com.regent.rpush.route.controller;

import com.regent.rpush.api.server.ServerInfoService;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.rpushserver.ServerInfoDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 路由Controller
 *
 * @author 钟宝林
 * @since 2021/2/26/026 21:14
 **/
@RequestMapping("/route")
@RestController
public class RouteController {

    @Autowired
    private ServerInfoService serverInfoService;

    @ApiOperation("路由，返回某个可用的服务器信息")
    @GetMapping
    public ApiResult<ServerInfoDTO> route() {
        return ApiResult.of(serverInfoService.serverInfo().getData());
    }

}
