package com.regent.rpush.api.server;

import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.message.NormalMessageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "rpush-server", contextId = "MessagePushService")
@RequestMapping("/push")
public interface MessagePushService {

    @PostMapping
    ApiResult<String> push(@RequestBody NormalMessageDTO message);

}
