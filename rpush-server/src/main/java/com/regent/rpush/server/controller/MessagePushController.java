package com.regent.rpush.server.controller;

import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.message.MessagePushDTO;
import com.regent.rpush.server.api.MessagePushService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessagePushController implements MessagePushService {

    public ApiResult<String> push(MessagePushDTO rMessage) {
        return ApiResult.of("消息发送成功");
    }

}
