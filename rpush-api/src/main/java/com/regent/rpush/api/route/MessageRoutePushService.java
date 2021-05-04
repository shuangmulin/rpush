package com.regent.rpush.api.route;

import com.regent.rpush.dto.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 消息路由投递服务
 *
 * @author 钟宝林
 * @since 2021/4/23/023 22:35
 **/
@FeignClient(name = "rpush-route", contextId = "MessageRoutePushService")
@RequestMapping("/message/push")
public interface MessageRoutePushService {

    @PostMapping
    ApiResult<String> push(@RequestBody String param);

}
