package com.regent.rpush.server.api;

import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.message.MessagePushDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "rpush-server")
@RequestMapping("/push")
public interface MessagePushService {

    @PostMapping
    ApiResult<String> push(@RequestBody MessagePushDTO rMessage);

}
