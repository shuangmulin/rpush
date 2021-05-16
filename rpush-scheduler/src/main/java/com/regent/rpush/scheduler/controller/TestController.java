package com.regent.rpush.scheduler.controller;

import com.regent.rpush.api.route.MessageRoutePushService;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.scheduler.auth.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 钟宝林
 * @since 2021/4/23/023 22:29
 **/
@RestController
@RequestMapping("/test")
@PreAuthorize("hasAnyAuthority('admin')")
public class TestController {

    @Autowired
    private MessageRoutePushService messageRoutePushService;

    @GetMapping
    public ApiResult<String> test() {
        String clientId = SessionUtils.getClientId();
        messageRoutePushService.push("fsdf");
        return ApiResult.success();
    }
}
